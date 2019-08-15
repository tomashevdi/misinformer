#include "messagelistmodel.h"

MessageListModel::MessageListModel(QObject *parent, QWidget* w) : QAbstractListModel(parent)
{
    this->pWidget = w;
}

void MessageListModel::addMessage(Message& msg)
{
    msgList.insert(0,msg);
    emit dataChanged(this->index(0), this->index(msgList.size()-1));
}

const Message MessageListModel::getMessage(int index)
{
    return msgList.at(index);
}

void MessageListModel::markRead(int index, int value)
{
    Message msg = msgList.at(index);

    QString answer = "";
    if (!(msg.needAnswer=="")) {
        QInputDialog d(pWidget);
        d.setInputMode(QInputDialog::InputMode::TextInput);
        d.setLabelText(msg.needAnswer);
        d.setWindowTitle("Требуется ответ на сообщение");
        d.setOkButtonText("Ок");
        d.setCancelButtonText("Отмена");
        d.resize(400,250);
        if (!d.exec()) return;
        answer = d.textValue();
    }

    if (msg.important) {
        QMessageBox messageBox(QMessageBox::Warning,
                    "Подтверждение","Данное сообщение было отмечено как ВАЖНОЕ. Вы уверены что точно ознакомились с информацией?",
                    QMessageBox::Yes | QMessageBox::No,
                    pWidget);
            messageBox.setButtonText(QMessageBox::Yes, "Да");
            messageBox.setButtonText(QMessageBox::No, "Нет");
            messageBox.setDefaultButton(QMessageBox::No);
        if (messageBox.exec()==QMessageBox::No) {
            return;
        }
    }
    msg.readMark = value;
    msgList.replace(index, msg);
    emit dataChanged(this->index(index), this->index(index));

    QUrl url;
    url.setScheme("http");
    url.setHost(server);
    url.setPort(port);
    url.setPath("/app/messages/"+QString::number(msg.id));
    url.setQuery("user="+user+"&computer="+computer+"&answer="+answer);

    QNetworkRequest req;
    req.setUrl(url);

    QNetworkAccessManager *restclient;
    restclient = new QNetworkAccessManager(this);
    QNetworkReply *reply = restclient->get(req);

    QEventLoop synchronous;
    connect(reply, &QNetworkReply::finished, &synchronous, &QEventLoop::quit);
    synchronous.exec();

    reply->close();
    reply->deleteLater();
}

int MessageListModel::nextUnread()
{
    if (msgList.size()==0) return -1;
    for( int i=msgList.size()-1; i >=0; --i ) {
        if (msgList[i].readMark==0) return i;
    }
    return -1;
}

void MessageListModel::loadMessages()
{
    QUrl url;
    url.setScheme("http");
    url.setHost(server);
    url.setPort(port);
    url.setPath("/app/messages");
    url.setQuery("user="+user+"&computer="+computer);

    QNetworkRequest req;
    req.setUrl(url);

    QNetworkAccessManager *restclient;
    restclient = new QNetworkAccessManager(this);
    QNetworkReply *reply = restclient->get(req);

    QEventLoop synchronous;
    connect(reply, &QNetworkReply::finished, &synchronous, &QEventLoop::quit);
    synchronous.exec();

    QJsonObject obj = parseReply(reply);
    reply->close();
    reply->deleteLater();

    foreach (const QJsonValue& m, obj["arr"].toArray()) {
        QJsonObject o = m.toObject();
        Message msg;
        msg.id = o["id"].toInt();
        msg.author = o["author"].toString();
        msg.date = o["date"].toString();
        msg.subject = o["subject"].toString();
        msg.important = o["important"].toBool();
        msg.readMark = o["readMark"].toBool();
        msg.needAnswer = o["needAnswer"].toString();
        msg.text = o["text"].toString();
        this->addMessage(msg);
     }
}

QJsonObject MessageListModel::parseReply(QNetworkReply *reply)
{
    QJsonObject jsonObj;
    QJsonDocument jsonDoc;
    QJsonParseError parseError;
    auto replyText = reply->readAll();
    jsonDoc = QJsonDocument::fromJson(replyText, &parseError);
    if(parseError.error != QJsonParseError::NoError){
        exit(1);
    }else{
        if(jsonDoc.isObject())
            jsonObj  = jsonDoc.object();
        else if (jsonDoc.isArray())
            jsonObj["arr"] = jsonDoc.array();
    }
    return jsonObj;
}

int MessageListModel::rowCount(const QModelIndex &parent) const
{
    Q_UNUSED(parent);
    return msgList.size();
}

QVariant MessageListModel::data(const QModelIndex &index, int role) const
{
    Message msg = msgList.at(index.row());
    if (role == Qt::DisplayRole) {
        return msg.getTitle();
    }
    if (role == Qt::DecorationRole) {
        if (msg.readMark > 0 ) return QVariant::fromValue(QColor(Qt::green));
        if (msg.important) return QVariant::fromValue(QColor(Qt::red));
        return QVariant::fromValue(QColor(Qt::blue));
    }
    return QVariant();
}

bool MessageListModel::getNames()
{
    WCHAR userName [ UNLEN + 1 ];
    WCHAR computerName [ UNLEN + 1 ];
    DWORD userSize = UNLEN + 1;
    DWORD computerSize = UNLEN + 1;
    if (GetUserNameW( userName, &userSize )) {
        user = QString::fromWCharArray(userName);
        qDebug() << user;
    }
    else
        return false;
    if (GetComputerNameW(computerName, &computerSize )) {
        computer = QString::fromWCharArray(computerName);
        qDebug() << computer;
    }
    else
        return false;

    return true;
}
