//
//  AkMembersCardDetailView.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "AkMembersCardModel.h"
//#import "AllMenuPopupView.h"

@interface AkMembersCardDetailView : UWNavigation<baseModelDelegate>



@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@property (nonatomic, retain) UIWebView *webView;
@property (nonatomic) int nWebViewType;
//@property (nonatomic, retain) AllMenuPopupView *menuPopup;


@end
