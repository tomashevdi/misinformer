#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(int startUp, QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    this->startUp = startUp;

    msgList = new MessageListModel(this,this);
    if (!msgList->getNames()) exit(1);
    msgList->loadMessages();

    if (startUp && msgList->nextUnread()==-1) exit(0);

    ui->setupUi(this);

    ui->msgList->setModel(msgList);

    if (startUp) {
        Qt::WindowFlags flags = windowFlags();
        flags = flags & (~Qt::WindowCloseButtonHint);
        flags = flags & (~Qt::WindowMaximizeButtonHint);
        flags = flags & (~Qt::WindowMinimizeButtonHint);
        setWindowFlags(flags);
        this->setWindowFlags(flags | Qt::WindowStaysOnTopHint);
    }
    nextUnread();

    ui->msgText->setOpenLinks(false);
    ui->msgText->setOpenExternalLinks(false);
    ui->msgText->setReadOnly(false);
    ui->msgText->setTextInteractionFlags(ui->msgText->textInteractionFlags() | Qt::LinksAccessibleByMouse);
}


MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_btnRead_clicked()
{
    QModelIndexList i  = ui->msgList->selectionModel()->selectedRows();
    if (i.size()==1) {
        msgList->markRead(i.at(0).row(),1);
        ui->btnRead->setEnabled(false);
        nextUnread();
    }
}

void MainWindow::messageSelected(QModelIndex i)
{
    selectMessage(i.row());
}

void MainWindow::on_btnPrint_clicked()
{
    QPrinter printer;
    ui->msgText->print(&printer);
}

int MainWindow::nextUnread()
{
    int idx = msgList->nextUnread();
    if (startUp && idx==-1) qApp->exit();
    if (idx!=-1) {
        ui->msgList->selectionModel()->select(msgList->index(idx),QItemSelectionModel::ClearAndSelect);
        selectMessage(idx);
    }
    return idx;
}

void MainWindow::selectMessage(int idx)
{
    Message msg = msgList->getMessage(idx);
    ui->msgText->setHtml(msg.getMessage());
    if (msg.readMark) {
       ui->btnRead->setEnabled(false);
    } else {
       ui->btnRead->setEnabled(true);
    }
    ui->btnPrint->setEnabled(true);
}


void MainWindow::closeEvent (QCloseEvent *event)
{
    if (startUp==2) {
        event->ignore();
    } else if (startUp==1) {

        QMessageBox::StandardButton resBtn = QMessageBox::question( this, "Информатор",
                                                                "Вы не ознакомились со всеми сообщениями. Вы уверены, что хотите выйти?",
                                                                QMessageBox::No | QMessageBox::Yes,
                                                                QMessageBox::No);
        if (resBtn != QMessageBox::Yes) {
            event->ignore();
        } else {
            event->accept();
        }
    } else {
            event->accept();
    }
}

void MainWindow::on_msgText_anchorClicked(const QUrl &url)
{
       QDesktopServices::openUrl(url);
}
