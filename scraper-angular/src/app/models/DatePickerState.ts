export class DatePickerState {
  private pLabel: string;
  private pActionType: string;

  constructor(pLabel, pActionType) {
    this.pLabel = pLabel;
    this.pActionType = pActionType;
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
