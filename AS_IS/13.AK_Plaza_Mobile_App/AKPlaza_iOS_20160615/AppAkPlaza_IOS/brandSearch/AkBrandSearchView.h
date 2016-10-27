//
//  AkBrandSearchView.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "AkBrandSearchModel.h"


@interface AkBrandSearchView : UWNavigation <baseModelDelegate>


@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@property (nonatomic, retain) UIWebView *webView;




@end
