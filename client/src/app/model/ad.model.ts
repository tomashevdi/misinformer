export class ADObject {
    guid: string;
    dn: string;
    name: string;
    notFlag: boolean = false;
}

export class ADUser extends ADObject {
    samaccountName: string;
    members: string[];
}