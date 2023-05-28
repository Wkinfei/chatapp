import { ViewportScroller } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent {

  constructor(
    private viewportScroller: ViewportScroller,
  ){}


  onClickScroll(sectionId: string) {
    // this.viewportScroller.setHistoryScrollRestoration("manual");
     this.viewportScroller.scrollToAnchor(sectionId);
    //  this.viewportScroller.setHistoryScrollRestoration("auto");
  }

}
