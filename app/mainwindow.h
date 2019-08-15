#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtPrintSupport>
#include "messagelistmodel.h"
#include <iostream>
#include <QCloseEvent>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(int startUp = 0, QWidget *parent = 0);
    ~MainWindow();

protected:
    void closeEvent (QCloseEvent *event);

private slots:
    void on_btnRead_clicked();
    void messageSelected(QModelIndex);
    void on_btnPrint_clicked();
    void on_msgText_anchorClicked(const QUrl &url);

private:
    Ui::MainWindow *ui;
    MessageListModel *msgList;
    int startUp = 0;
    void loadMessages();
    void selectMessage(int idx);
    int nextUnread();
};

#endif // MAINWINDOW_H
