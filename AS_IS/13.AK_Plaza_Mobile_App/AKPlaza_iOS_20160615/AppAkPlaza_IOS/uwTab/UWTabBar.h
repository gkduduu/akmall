//
//  LPTabBar.h
//  YFPortal
//
//  Created by Jong Pil Park on 10. 11. 3..
//  Copyright 2010 Lilac Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWTabBarItem.h"
#import "CustomBadge.h"

@protocol UWTabBarViewDelegate;

@interface UWTabBar : UIViewController <UWTabBarItemDelegate> {
	int initTab;
	id <UWTabBarViewDelegate> delegate;
    BOOL isTabBarHidden;
	UIView *tabBarHolder;
	NSMutableArray *tabViewControllers;
	NSMutableArray *tabItemsArray;
    CustomBadge *customBadge;
}

@property int initTab;
@property (nonatomic, retain) UIView *tabBarHolder;
@property (nonatomic, retain) UIWebView *webView;
@property (nonatomic) NSInteger *currentIndex;
@property (nonatomic, assign) id <UWTabBarViewDelegate> delegate;
@property (nonatomic, retain) NSMutableArray *tabViewControllers;
@property (nonatomic, retain) NSMutableArray *tabItemsArray;
@property (nonatomic) BOOL isTabBarHidden;
@property (nonatomic) BOOL flag;

- (id)initWithTabViewControllers:(NSMutableArray *)tbControllers tabItems:(NSMutableArray *)tbItems initialTab:(int)iTab;
- (void)initialTab:(int)tabIndex;
- (void)activateController:(int)index;
- (void)activateTabItem:(int)index;
- (void)setBadge:(int)cnt;

@end

@protocol UWTabBarViewDelegate
@optional
- (void)showTabBar;
- (void)hideTabBar;
- (void)addController:(id)controller;
@end
