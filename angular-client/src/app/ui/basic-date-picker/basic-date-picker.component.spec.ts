import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicDatePickerComponent } from './basic-date-picker.component';

describe('BasicDatePickerComponent', () => {
  let component: BasicDatePickerComponent;
  let fixture: ComponentFixture<BasicDatePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BasicDatePickerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicDatePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
