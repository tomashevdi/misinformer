/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.10.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QListView>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QTextBrowser>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralWidget;
    QTextBrowser *msgText;
    QPushButton *btnRead;
    QPushButton *btnPrint;
    QListView *msgList;
    QLabel *label;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QStringLiteral("MainWindow"));
        MainWindow->resize(1003, 538);
        QIcon icon;
        icon.addFile(QStringLiteral(":/icon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        MainWindow->setWindowIcon(icon);
        centralWidget = new QWidget(MainWindow);
        centralWidget->setObjectName(QStringLiteral("centralWidget"));
        msgText = new QTextBrowser(centralWidget);
        msgText->setObjectName(QStringLiteral("msgText"));
        msgText->setGeometry(QRect(290, 40, 701, 451));
        QFont font;
        font.setPointSize(11);
        msgText->setFont(font);
        btnRead = new QPushButton(centralWidget);
        btnRead->setObjectName(QStringLiteral("btnRead"));
        btnRead->setEnabled(false);
        btnRead->setGeometry(QRect(570, 500, 421, 31));
        QFont font1;
        font1.setPointSize(11);
        font1.setBold(true);
        font1.setWeight(75);
        btnRead->setFont(font1);
        btnPrint = new QPushButton(centralWidget);
        btnPrint->setObjectName(QStringLiteral("btnPrint"));
        btnPrint->setEnabled(false);
        btnPrint->setGeometry(QRect(290, 500, 131, 31));
        QFont font2;
        font2.setPointSize(10);
        font2.setBold(true);
        font2.setWeight(75);
        btnPrint->setFont(font2);
        msgList = new QListView(centralWidget);
        msgList->setObjectName(QStringLiteral("msgList"));
        msgList->setGeometry(QRect(10, 40, 271, 491));
        QFont font3;
        font3.setPointSize(10);
        msgList->setFont(font3);
        label = new QLabel(centralWidget);
        label->setObjectName(QStringLiteral("label"));
        label->setGeometry(QRect(10, 10, 981, 16));
        label->setFont(font1);
        MainWindow->setCentralWidget(centralWidget);

        retranslateUi(MainWindow);
        QObject::connect(msgList, SIGNAL(clicked(QModelIndex)), MainWindow, SLOT(messageSelected(QModelIndex)));

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "\320\241\320\237\320\261 \320\223\320\221\320\243\320\227 \320\223\320\237 \342\204\226106 \320\230\320\275\321\204\320\276\321\200\320\274\320\260\321\202\320\276\321\200", nullptr));
        btnRead->setText(QApplication::translate("MainWindow", "\320\257 \320\277\321\200\320\276\321\207\320\270\321\202\320\260\320\273 \320\270 \320\276\320\267\320\275\320\260\320\272\320\276\320\274\320\270\320\273\321\201\321\217 \321\201 \320\270\320\275\321\204\320\276\321\200\320\274\320\260\321\206\320\270\320\265\320\271", nullptr));
        btnPrint->setText(QApplication::translate("MainWindow", "\320\240\320\260\321\201\320\277\320\265\321\207\320\260\321\202\320\260\321\202\321\214", nullptr));
        label->setText(QApplication::translate("MainWindow", "\320\224\320\273\321\217 \320\267\320\260\320\272\321\200\321\213\321\202\320\270\321\217 \320\276\320\272\320\275\320\260 \320\270\320\275\321\204\320\276\321\200\320\274\320\260\321\202\320\276\321\200\320\260 \320\276\320\267\320\275\320\260\320\272\320\276\320\274\321\214\321\202\320\265\321\201\321\214 \321\201\320\276 \320\262\321\201\320\265\320\274\320\270 \320\275\320\265\320\277\321\200\320\276\321\207\320\270\321\202\320\260\320\275\320\275\321\213\320\274\320\270 \321\201\320\276\320\276\320\261\321\211\320\265\320\275\320\270\321\217\320\274\320\270 (\320\272\321\200\320\260\321\201\320\275\321\213\320\271/\321\201\320\270\320\275\320\270\320\271)!", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
