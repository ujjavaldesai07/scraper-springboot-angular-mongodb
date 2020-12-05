import {DropdownOption} from './DropdownOption';

export class DropdownState {
  options: DropdownOption[];
  label: string;
  actionType: string;

  constructor(options, label, type) {
    this.options = options;
    this.label = label;
    this.actionType = type;
  }
}
