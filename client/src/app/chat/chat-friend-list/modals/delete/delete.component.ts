import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.css']
})
export class DeleteComponent {

  @Input() 
  id!: number;

  @Input()
  name!:string;

  constructor( public modal: NgbActiveModal) {}

  onOkay() {
    this.modal.close(this.id);
  }

}