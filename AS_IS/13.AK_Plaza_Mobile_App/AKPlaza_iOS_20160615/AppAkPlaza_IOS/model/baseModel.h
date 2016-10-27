//
//  baseModel.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>



@protocol baseModelDelegate;

@interface baseModel : NSObject <UIWebViewDelegate>
{
    UIActivityIndicatorView *act;
    BOOL isHeaderModify;

}

@property (nonatomic, assign) id<baseModelDelegate> delegate;


- (void)activityStart;
- (void)activityStop;
@end



@protocol baseModelDelegate <NSObject>

@required

- (void)activityStartView:(UIActivityIndicatorView *)act;


@optional
- (UINavigationController *)navigationControllerSetting;
- (void)actionGoBack;


@end
