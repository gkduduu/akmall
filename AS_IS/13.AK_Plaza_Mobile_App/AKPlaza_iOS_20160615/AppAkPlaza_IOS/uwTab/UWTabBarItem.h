//
//  UWTabBarItem.h
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 6. 22..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol UWTabBarItemDelegate;

@interface UWTabBarItem : UIButton {
    
    id <UWTabBarItemDelegate>delegate;
    BOOL isTabItemOn;
    
}

@property (nonatomic, assign) id <UWTabBarItemDelegate> delegate;
@property (nonatomic) BOOL isTabItemOn;

// 컨트로의 사이즈를 강제로 적절하게 조절한다. 프레임 사이즈는 무시됨!
-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t ; 
-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t txtNormalState:(NSString *)tn txtToggledState:(NSString *)tt ;
-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t txtNormalState:(NSString *)tn txtToggledState:(NSString *)tt 
    txtNormalColor:(UIColor*)tnc txtToggledColor:(UIColor*)ttc ;

-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t  andAccessibilityLabel:(NSString*)accessibilityLabel;


- (BOOL)doTabItemOn;
- (void)toggleOn:(BOOL)state;

@end



@protocol UWTabBarItemDelegate
-(void)selectedItem:(UWTabBarItem *)button;
@end
