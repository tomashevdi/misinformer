import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'msgFilter',
  pure: false
})

export class MsgFilterPipe implements PipeTransform {
  transform(items: any[], args: any[]): any {
    if (args) {
      return items.filter(item => item.stopDate === null || item.stopDate === '' || (new Date(item.stopDate)).getTime() >= (new Date()).getTime() );
    }
    return items;
  }
}
