export class MessageGroup {
    id: number;
    name: string;
    predefined: boolean;
    comment: string;
    items: MessageGroupItem[] = [];

    notFlag: boolean;
}

export class MessageGroupItem {
    id: number;
    itemType: string;
    guid: string;
    msgGroupId: number;
    notFlag: boolean = false;
}
