export class DropdownOption {
  private pText: string;
  private pValue: string;

  constructor(pText: string, pValue: string) {
    this.pText = pText;
    this.pValue = pValue;
  }

  get text(): string {
    return this.pText;
  }

  set text(pText: string) {
    this.pText = pText;
  }

  get value(): string {
    return this.pValue;
  }

  set value(pValue: string) {
    this.pValue = pValue;
  }
}
