#include "mainwindow.h"
#include <QApplication>
#include "windows.h"
#include <stdio.h>
#include <stdlib.h>


int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    QLocale curLocale(QLocale("ru_RU"));
    QLocale::setDefault(curLocale);

    int startUp = argc > 1 ? atoi(argv[1]) : false;
    MainWindow w(startUp);
    w.show();
    return a.exec();
}
