//
//  AkMembersCardDetailView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkMembersCardDetailView.h"

@implementation AkMembersCardDetailView
@synthesize activityView;
@synthesize webView;
@synthesize nWebViewType;
//@synthesize menuPopup;

#pragma mark - View lifecycle end
-(void)dealloc
{
    [activityView release];
    [webView release];
    //[menuPopup release];
    
    [super dealloc];
}

- (void)viewDidUnload
{
    self.activityView = nil;
    self.webView = nil;
    //self.menuPopup = nil;
    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - event


- (void)doRightButton:(id)sender
{
    //팝업메뉴 띄우기 
    //[self.menuPopup animate:sender];
}




- (void) doLeftButton:(id)sender
{
    if (self.webView.canGoBack) {
        [self.webView goBack];
    }
    else
    {
        [self.navigationController popToRootViewControllerAnimated:YES];
    }
}


#pragma mark - View lifecycle start

- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation_web" owner:self options:NULL] lastObject];
}

- (void)viewWillAppear:(BOOL)animate
{
    [super viewWillAppear:YES];
    
    //self.menuPopup.alpha = 0.0;
    
    /*
    if (self.nWebViewType == 0) {
        AkMembersCardModel *oModel = [[AkMembersCardModel alloc] init];
        
        oModel.delegate = self;
        
        self.webView = [oModel subMyMileageListWebView];
        
        self.webView.frame = CGRectMake(0, 0, self.contentView.frame.size.width, self.contentView.frame.size.height);
        
        [self.contentView addSubview:self.webView];
        
        //[oModel release];
        //oModel = nil;
    }
    else
    {
     */
        AkMembersCardModel *oModel = [[AkMembersCardModel alloc] init];
        
        oModel.delegate = self;
    
        if(self.webView == nil){
            self.webView = [oModel subCardInfoWebView];
            self.webView.frame = self.contentView.frame;
            [self.contentView addSubview:self.webView];
        }
    
        self.webView = [oModel subCardInfoWebView];
        
        self.webView.frame = self.contentView.frame;
        [self.webView setBackgroundColor:[UIColor whiteColor]];
        
        [self.contentView addSubview:self.webView];
        
        //[oModel release];
        //oModel = nil;
    /*
    }
    */

}


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];

//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_all" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_all_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    
    /*
    self.menuPopup = [[AllMenuPopupView alloc ] init];
    
    self.menuPopup.frame = CGRectMake(5, 5, 310, 400);
    
    [self.view addSubview:self.menuPopup];
     */

    
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
