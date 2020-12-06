import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartDatePickerComponent } from './start-date-picker.component';

describe('StartDatePickerComponent', () => {
  let component: StartDatePickerComponent;
  let fixture: ComponentFixture<StartDatePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StartDatePickerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StartDatePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
