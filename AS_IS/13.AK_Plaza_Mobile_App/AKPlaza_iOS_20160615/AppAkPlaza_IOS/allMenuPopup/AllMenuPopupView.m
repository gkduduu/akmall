//
//  AllMenuPopupView.m
//  AppAkPlaza_iOS
//
//  Created by  on 11. 12. 9..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AllMenuPopupView.h"
#import "UIView-ModalAnimationHelper.h"
@implementation AllMenuPopupView
@synthesize btnClose;
@synthesize menuWebview;
@synthesize activityView;
@synthesize navi;


#define COOKBOOK_PURPLE_COLOR	[UIColor colorWithRed:0.20392f green:0.19607f blue:0.61176f alpha:1.0f]
#define BARBUTTON(TITLE, SELECTOR) 	[[[UIBarButtonItem alloc] initWithTitle:TITLE style:UIBarButtonItemStylePlain target:self action:SELECTOR] autorelease]


- (void)dealloc
{
    
    [menuWebview release];
    [btnClose release];
    [activityView release];
    [navi release];
    
    [super dealloc];
}





-(id)init
{
    self = [[[NSBundle mainBundle] loadNibNamed:@"AllMenuPopupView" owner:self options:NULL] lastObject];
    
    if (self) {
        
        /*
         CoreAnimation: failed to allocate 702496 bytes
         AppAkPlaza_iOS(77808,0xac0772c0) malloc: *** mmap(size=524288) failed (error code=12)
         *** error: can't allocate region
         *** set a breakpoint in malloc_error_break to debug
         
         */
        //self.transform = CGAffineTransformMakeScale(0.01f, 0.01f);
        self.alpha = 0.0f;
        
        
        AllMenuPopupModel *oModel = [[AllMenuPopupModel alloc] init];
        
        oModel.delegate = self;
        
        self.menuWebview = [oModel allMenuPopupWebView];
        
        self.menuWebview.layer.cornerRadius = 5.0f;
        self.menuWebview.frame = CGRectMake(3, 35, 304, 362);
        self.menuWebview.clipsToBounds = YES;
        self.menuWebview.scalesPageToFit = YES;
        

        [self addSubview:self.menuWebview];
        
    }
    
    return self;
}






- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
   //self = [[[NSBundle mainBundle] loadNibNamed:@"AllMenuPopupView" owner:self options:NULL] lastObject];
    
    
    if (self) {
        
        //self.transform = CGAffineTransformMakeScale(0.01f, 0.01f);
        self.alpha = 0.0f;
        
        [self.menuWebview loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://www.google.com"]]];
    }
    return self;
}


- (void) hide: (id) sender
{
    // Slowly zoom back down and hide the view
	[UIView beginAnimations:nil context:UIGraphicsGetCurrentContext()];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	[UIView setAnimationDuration:0.3f];
	self.transform = CGAffineTransformMakeScale(0.01f, 0.01f);
	[UIView commitModalAnimations];
	
	// Restore the bar button
	self.alpha = 0.0f;
	//self.navigationItem.rightBarButtonItem = BARBUTTON(@"Bounce", @selector(animate:));
}

- (void) animate: (id) sender
{
	// Hide the bar button and show the view
	//self.navigationItem.rightBarButtonItem = nil;
	self.alpha = 1.0f;
	
    self.layer.cornerRadius = 5.0f;
    self.backgroundColor = [UIColor grayColor];
    
	// Bounce to 115% of the normal size
	[UIView beginAnimations:nil context:UIGraphicsGetCurrentContext()];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	[UIView setAnimationDuration:0.4f];
	self.transform = CGAffineTransformMakeScale(1.15f, 1.15f);
    
	[UIView commitModalAnimations];
    
	// Return back to 100%
	[UIView beginAnimations:nil context:UIGraphicsGetCurrentContext()];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	[UIView setAnimationDuration:0.3f];
	self.transform = CGAffineTransformMakeScale(1.0f, 1.0f);
	[UIView commitModalAnimations];
    
    
    [self.menuWebview goBack];
	
	// Pause for a second and appreciate the presentation
	[NSThread sleepUntilDate:[NSDate dateWithTimeIntervalSinceNow:1.0f]];
	
    
    
    
    
    //[(UIButton *)[self viewWithTag:888] addTarget:self action:@selector(hide:) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
}




#pragma mark - baseModel



-(void)activityStartView:(UIActivityIndicatorView *)act
//-(void)activityStartView:(MBProgressHUD *)act
{
    //로딩 화면 출력될 때 ActivityIndicatorView 보이기
	self.activityView = act;
    
    //changuk 2013.08.26 정중앙으로 변경
//	self.activityView.center = CGPointMake(160, 169);
    self.activityView.center = self.center;
	
    [self insertSubview:self.activityView atIndex:999];
    
    [self.activityView startAnimating];
}


-(UINavigationController *)navigationControllerSetting
{
    return self.navi;
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
