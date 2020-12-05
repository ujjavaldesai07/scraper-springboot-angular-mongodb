import {DropdownOption} from './DropdownOption';

/**
 * Creates dropdown state which takes
 * options, label, actionType
 */
export class DropdownState {
  private pOptions: DropdownOption[];
  private pLabel: string;
  private pActionType: string;

  constructor(pOptions, pLabel, pActionType) {
    this.pOptions = pOptions;
    this.pLabel = pLabel;
    this.pActionType = pActionType;
  }

  get options(): DropdownOption[] {
    return this.pOptions;
  }

  set options(options: DropdownOption[]) {
    this.pOptions = options;
  }

  get label(): string {
    return this.pLabel;
  }

  set label(pLabel: string) {
    this.pLabel = pLabel;
  }

  get actionType(): string {
    return this.pActionType;
  }

  set actionType(pActionType: string) {
    this.pActionType = pActionType;
  }
}
