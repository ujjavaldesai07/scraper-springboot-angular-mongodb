import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TableComponent} from './table/table.component';
import {HttpClientModule} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {MatTableModule} from '@angular/material/table';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatNativeDateModule} from '@angular/material/core';
import {BasicDatePickerComponent} from './ui/basic-date-picker/basic-date-picker.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSelectModule} from '@angular/material/select';
import {BasicDropDownComponent} from './ui/basic-drop-down/basic-drop-down.component';
import {MatGridListModule} from '@angular/material/grid-list';
import {FilterLocationComponent} from './filters/filter-location/filter-location.component';
import {FilterWebsiteComponent} from './filters/filter-website/filter-website.component';
import {SortEventsComponent} from './sort/sort-events/sort-events.component';
import {StartDatePickerComponent} from './filters/dates/start-date-picker/start-date-picker.component';
import {EndDatePickerComponent} from './filters/dates/end-date-picker/end-date-picker.component';
import {MatIconModule} from '@angular/material/icon';
import {StoreModule} from '@ngrx/store';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {HomeComponent} from './views/home/home.component';
import {ErrorComponent} from './errors/error/error.component';
import {MatButtonModule} from '@angular/material/button';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

import {tableReducer} from './reducers/tableReducer';
import {errorReducer} from './reducers/errorReducer';

@NgModule({
  declarations: [
    // components
    AppComponent,
    TableComponent,
    BasicDatePickerComponent,
    BasicDropDownComponent,
    FilterLocationComponent,
    FilterWebsiteComponent,
    SortEventsComponent,
    StartDatePickerComponent,
    EndDatePickerComponent,
    HomeComponent,
    ErrorComponent
  ],
  imports: [
    // modules
    BrowserModule,
    HttpClientModule,
    CommonModule,
    MatTableModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    BrowserAnimationsModule,
    MatSelectModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,

    // reducers
    StoreModule.forRoot({tableReducer, errorReducer}),

    // this is used to configure redux dev tools for analyzing states
    StoreDevtoolsModule.instrument({
      maxAge: 10 // saves upto 10 records in redux dex tools
    }),
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
