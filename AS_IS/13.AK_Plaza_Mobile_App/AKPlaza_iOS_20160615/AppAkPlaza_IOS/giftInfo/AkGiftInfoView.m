//
//  AkGiftInfoView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkGiftInfoView.h"

@implementation AkGiftInfoView

@synthesize activityView;
@synthesize webView;


#pragma mark - View lifecycle end
-(void)dealloc
{
    [activityView release];
    [webView release];

    
    [super dealloc];
}

- (void)viewDidUnload
{
    self.activityView = nil;
    self.webView = nil;

    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


#pragma mark - event
- (void)doRightButton:(id)sender
{
    //팝업메뉴 띄우기 
    //[self.menuPopup animate:sender];
}




#pragma mark - View lifecycle start

- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation_web" owner:self options:NULL] lastObject];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    //self.menuPopup.alpha = 0.0;
    
    
    AkGiftInfoModel *oModel = [[AkGiftInfoModel alloc] init];
    
    oModel.delegate = self;
    
    if(self.webView == nil){
        self.webView = [oModel mainGiftInfoWebView];
        self.webView.frame = self.contentView.frame;
        [self.contentView addSubview:self.webView];
    }
    
    self.webView = [oModel mainGiftInfoWebView];
    
    self.webView.frame = CGRectMake(0, 0, self.contentView.frame.size.width, self.contentView.frame.size.height);
    [self.webView setBackgroundColor:[UIColor whiteColor]];
    [self.contentView addSubview:self.webView];
    
    //[oModel release];
    //oModel = nil;
}


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_all" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_all_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    


}




- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}




#pragma mark - baseModel

-(UINavigationController *)navigationControllerSetting
{
    return self.navigationController;
}

-(void)activityStartView:(UIActivityIndicatorView *)act
//-(void)activityStartView:(MBProgressHUD *)act
{
    //로딩 화면 출력될 때 ActivityIndicatorView 보이기
	self.activityView = act;
    
    //changuk 2013.08.26 정중앙으로 변경
//	self.activityView.center = CGPointMake(160, 169);
    self.activityView.center = self.contentView.center;
	
    [self.contentView insertSubview:self.activityView atIndex:999];
    
    [self.activityView startAnimating];
}


@end
