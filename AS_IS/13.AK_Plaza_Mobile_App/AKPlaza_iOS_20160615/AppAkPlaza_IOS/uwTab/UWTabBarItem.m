//
//  UWTabBarItem.m
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 6. 22..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "UWTabBarItem.h"


@implementation UWTabBarItem

@synthesize delegate;
@synthesize isTabItemOn;

-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t txtNormalState:(NSString *)tn txtToggledState:(NSString *)tt 
txtNormalColor:(UIColor*)tnc txtToggledColor:(UIColor*)ttc 
{
    self = [super initWithFrame:frame];
    
    if (self) {
        [self setTitle:tn                         forState:UIControlStateNormal];
        [self setTitle:tt                         forState:UIControlStateSelected];
        [self setTitleColor:tnc    forState:UIControlStateNormal];
        [self setTitleColor:ttc    forState:UIControlStateSelected];
        
        
        
        
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:n ofType:nil]] forState:UIControlStateNormal];
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:t ofType:nil]] forState:UIControlStateSelected];
        
        self.isTabItemOn = NO;
        
        [self addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    
    return self;
    
}


-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t txtNormalState:(NSString *)tn txtToggledState:(NSString *)tt 
{
    self = [super initWithFrame:frame];
    
    if (self) {
        [self setTitle:tn                         forState:UIControlStateNormal];
        [self setTitle:tt                         forState:UIControlStateSelected];
        [self setTitleColor:[UIColor whiteColor]    forState:UIControlStateNormal];
        [self setTitleColor:[UIColor blackColor]    forState:UIControlStateSelected];
        
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:n ofType:nil]] forState:UIControlStateNormal];
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:t ofType:nil]] forState:UIControlStateSelected];
        
        self.isTabItemOn = NO;
        
        [self addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    
    return self;
    
}

-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t 
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        //[self setAccessibilityLabel: NSLocalizedString(@"메롱", nil)];
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:n ofType:nil]] forState:UIControlStateNormal];
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:t ofType:nil]] forState:UIControlStateSelected];
        
        self.isTabItemOn = NO;
        
        [self addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    
    return self;
    
}


-(id)initWithFrame:(CGRect)frame ImgNormalState:(NSString *)n ImgToggledState:(NSString *)t  andAccessibilityLabel:(NSString*)accessibilityLabel
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        [self setAccessibilityLabel: NSLocalizedString(accessibilityLabel, nil)];
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:n ofType:nil]] forState:UIControlStateNormal];
        [self setBackgroundImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:t ofType:nil]] forState:UIControlStateSelected];
        
        self.isTabItemOn = NO;
        
        
        [self addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    
    return self;
    
}



-(BOOL)doTabItemOn
{
    return self.isTabItemOn;
}

-(void)toggleOn:(BOOL)state
{
    self.isTabItemOn = state;
    [self setSelected:self.isTabItemOn];
}



-(void)buttonPressed:(id)target
{
    [self.delegate selectedItem:target];
}

@end
