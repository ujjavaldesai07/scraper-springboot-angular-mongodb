import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterLocationComponent } from './filter-location.component';

describe('FilterLocationComponent', () => {
  let component: FilterLocationComponent;
  let fixture: ComponentFixture<FilterLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FilterLocationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
