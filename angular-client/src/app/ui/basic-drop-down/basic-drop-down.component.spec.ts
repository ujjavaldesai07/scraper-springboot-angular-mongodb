import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicDropDownComponent } from './basic-drop-down.component';

describe('BasicDropDownComponent', () => {
  let component: BasicDropDownComponent;
  let fixture: ComponentFixture<BasicDropDownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BasicDropDownComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicDropDownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
