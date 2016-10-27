//
//  AllMenuPopupView.h
//  AppAkPlaza_iOS
//
//  Created by  on 11. 12. 9..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "AllMenuPopupModel.h"

@interface AllMenuPopupView : UIView <baseModelDelegate>


@property (retain, nonatomic) IBOutlet UIButton *btnClose;
@property (retain, nonatomic) UIWebView *menuWebview;
@property (retain, nonatomic) UIActivityIndicatorView *activityView;
@property (retain, nonatomic) UINavigationController *navi;

- (IBAction)hide:(id)sender;
- (void) animate: (id) sender;

@end
