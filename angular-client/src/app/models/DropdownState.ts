import {DropdownOption} from './DropdownOption';

/**
 * Creates dropdown state which takes
 * options, label, actionType
 */
export class DropdownState {
  private pOptions: DropdownOption[];
  private pLabel: string;
  private pAttrName: string;
  private pActionType: string;
  private pErrorMsg: string;

  constructor(pOptions, pLabel, pActionType, pAttrName) {
    this.pOptions = pOptions;
    this.pLabel = pLabel;
    this.pActionType = pActionType;
    this.pAttrName = pAttrName;
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

  get errorMsg(): string {
    return this.pErrorMsg;
  }

  set errorMsg(pErrorMsg: string) {
    this.pErrorMsg = pErrorMsg;
  }

  get attrName(): string {
    return this.pAttrName;
  }

  set attrName(pAttrName: string) {
    this.pAttrName = pAttrName;
  }
}
