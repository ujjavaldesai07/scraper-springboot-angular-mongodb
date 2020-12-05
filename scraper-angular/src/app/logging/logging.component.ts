import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.css']
})
export class LoggingComponent implements OnInit {
  @Input() public log: any;

  constructor() { }

  ngOnInit(): void {
  }

}
