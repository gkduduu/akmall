//
//  UWCustomTabBar.h
//  UWCustomView
//
//  Created by 미영 신 on 11. 6. 4..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWTabBar.h"
#import "UWTabBarItem.h"
#import "ASIFormDataRequest.h"
#import "AkMainView.h"

@interface UWTabBarView : UIViewController<UWTabBarViewDelegate, UINavigationControllerDelegate, ASIHTTPRequestDelegate>{
    UWTabBar *tabView;
    AkMainView *tabViewController1;
}


@property (nonatomic, retain)  UWTabBar *tabView;


- (void)setUrl:(NSURL *)url;

@end

