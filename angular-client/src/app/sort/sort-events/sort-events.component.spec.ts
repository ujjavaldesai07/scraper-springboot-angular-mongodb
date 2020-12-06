import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SortEventsComponent } from './sort-events.component';

describe('SortEventsComponent', () => {
  let component: SortEventsComponent;
  let fixture: ComponentFixture<SortEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SortEventsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SortEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
