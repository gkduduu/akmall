//
//  UWNavigation.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AllMenuPopupView.h"

@interface UWNavigation : UIViewController<UIWebViewDelegate>
{
    UIButton *btnLeft;
    UIButton *btnRight;
    UIImageView *naviTitle;
    UIView *contentView;
    UIImageView *naviBackgroud;
}
- (IBAction)doLeftButton:(id)sender;
- (IBAction)doRightButton:(id)sender;

-(void)viewLblNaviTitle:(NSString *)title;

@property (nonatomic, retain) IBOutlet UIImageView *naviBackgroud;
@property (nonatomic, retain) IBOutlet UIButton *btnLeft;
@property (nonatomic, retain) IBOutlet UIButton *btnRight;
@property (nonatomic, retain) IBOutlet UIImageView *naviTitle;
@property (nonatomic, retain) IBOutlet UIView *contentView;
@property (retain, nonatomic) IBOutlet UIWebView *header;

//@property (nonatomic, retain) AllMenuPopupView *menuPopup;

@end
