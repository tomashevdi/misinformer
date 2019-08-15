#ifndef MESSAGELISTMODEL_H
#define MESSAGELISTMODEL_H

#include <QAbstractListModel>
#include <QMainWindow>
#include <QList>
#include <QColor>
#include <message.h>
#include <QDebug>
#include <QtNetwork>
#include <QMessageBox>
#include <QInputDialog>
#include "windows.h"
#include "lmcons.h"

class MessageListModel : public QAbstractListModel
{
    Q_OBJECT

public:
    MessageListModel(QObject *parent = 0, QWidget* w = 0);
    void addMessage(Message& msg);
    const Message getMessage(int index);
    void markRead(int index,  int value);
    int nextUnread();
    void loadMessages();
    bool getNames();
    int rowCount(const QModelIndex &parent) const;
    QVariant data(const QModelIndex &index, int role) const;
private:
    QString server = "informer.p106.local";
    int port = 7740;
    QString user;
    QString computer;

    QWidget* pWidget;

    QList<Message> msgList;
    QJsonObject parseReply(QNetworkReply *reply);
};

#endif // MESSAGELISTMODEL_H
