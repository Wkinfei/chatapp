import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GifDetails } from 'src/app/models/message';

@Component({
  selector: 'app-gif',
  templateUrl: './gif.component.html',
  styleUrls: ['./gif.component.css']
})
export class GifComponent {

  @Input()
  gifs!:GifDetails[]

  constructor( public modal: NgbActiveModal) {}

  onClick(gif: GifDetails) {
    this.modal.close(gif);
  }
}
