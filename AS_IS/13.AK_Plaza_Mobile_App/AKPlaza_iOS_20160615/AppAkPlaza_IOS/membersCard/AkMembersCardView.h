//
//  AkMembersCardView.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "AkMembersCardModel.h"


#import "fontSizeUtil.h"
#import "colorUtil.h"
#import "OLogin.h"
#import "OLogin+UserDefaults.h"
#import "LoginViewController.h"


@interface AkMembersCardView : UWNavigation <baseModelDelegate>
{

}



@property (nonatomic, retain) UIActivityIndicatorView *activityView;



- (void)clickSubMyMileageListView:(id)sender;
- (void)clickSubCardInfoView:(id)sender;


@end
