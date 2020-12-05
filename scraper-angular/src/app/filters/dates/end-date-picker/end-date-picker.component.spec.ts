import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndDatePickerComponent } from './end-date-picker.component';

describe('EndDatePickerComponent', () => {
  let component: EndDatePickerComponent;
  let fixture: ComponentFixture<EndDatePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EndDatePickerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EndDatePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
