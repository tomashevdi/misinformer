#ifndef MESSAGE_H
#define MESSAGE_H

#include <QString>

class Message
{
public:
    Message();

    int id = 0;
    QString author = "";
    QString date = "";
    QString subject = "";
    QString text = "";
    bool readMark = 0;
    bool needConfirm = false;
    bool important = false;
    QString needAnswer = "";

    QString getMessage() const;
    QString getTitle() const;
};

#endif // MESSAGE_H
