import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterWebsiteComponent } from './filter-website.component';

describe('FilterWebsiteComponent', () => {
  let component: FilterWebsiteComponent;
  let fixture: ComponentFixture<FilterWebsiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FilterWebsiteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterWebsiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
