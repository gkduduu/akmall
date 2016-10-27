
//
//  AkMainView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkMainView.h"
#import "AkBarcodeHistoryListView.h"
#import "AKAppDelegate.h"
#import "UWRequest.h"



@implementation AkMainView
@synthesize activityView;
@synthesize webView;
//@synthesize menuPopup;
@synthesize tabView;
@synthesize strURL;
//@synthesize progressView;

@synthesize isPhotoBack;

@synthesize oModel;

#define kBarcodeType @"barcode"
#define kMapType @"map"


#pragma mark - View lifecycle end
-(void)dealloc
{
    [activityView release];
    [webView release];
    //[menuPopup release];
    [tabView release];
    [strURL release];
    
    [oModel release];
//    [progressView release];
    
    [super dealloc];
}

- (void)viewDidUnload
{
    self.activityView = nil;
//    progressView = nil;
    self.webView = nil;
    //self.menuPopup = nil;
    self.tabView = nil;
    self.strURL = nil;
    
    self.oModel = nil;
    
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

/*

-(void)doBarcodeLeftButton:(id)sender
{
    [self dismissModalViewControllerAnimated:YES];
}

-(void)clickSubBarcodeHistory:(id)sender
{
    [self dismissModalViewControllerAnimated:YES];
    
    AkBarcodeHistoryListView *subView = [[AkBarcodeHistoryListView alloc] init];
    
    [self.navigationController pushViewController:subView animated:YES];
    
    subView = nil;

}

*/


-(void)clickSubGoogleMap:(id)sender
{
    
    //팝업 메뉴 숨기기
    //self.menuPopup.alpha = 0.0;
    
    
    strViewType = kMapType;
    
    self.strURL = [NSString stringWithFormat:@"%@/branch/branch.do?act=location&bc=01&isApp=Y", kRootURL];
    
    [self viewWillAppear:YES];
    
/*
    // 쿼리문 생성
    NSString* addressText = @"경기도 성남시 분당구 서현동 263";
    
    // 특수 문자 처리를 위해 인코딩을 합니다.
    NSString* searchQuery = [addressText stringByAddingPercentEscapesUsingEncoding:
                             NSUTF8StringEncoding];
    
    // URL 문자열을 생성합니다.
    NSString* urlString = [NSString stringWithFormat:
                           @"http://maps.google.com/maps?q=%@",
                           searchQuery];
    
    // 실행
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlString]];
*/    
    
}

- (void)clickSubLoginSettingView:(id)sender
{
    AkLoginWithNotiSettingView *subView = [[AkLoginWithNotiSettingView alloc] init];
    
    [self.navigationController pushViewController:subView animated:YES];
    
    subView = nil;
}


- (void)clickSubNotiListView:(id)sender
{
    AkNotiListBoxViewController *subView = [[AkNotiListBoxViewController alloc] init];
    
    [self.navigationController pushViewController:subView animated:YES];
    
    subView = nil;
}



- (void)clickReceiptBarcodeView:(id)sender
{
    AkReceiptBarcodeRegView *subView = [[AkReceiptBarcodeRegView alloc] init];
    
    [self.navigationController pushViewController:subView animated:YES];
    
    subView = nil;
   
}



-(void)doLeftButton:(id)sender
{
    
}


#pragma mark - View lifecycle start


// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation_web" owner:self options:NULL] lastObject];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    self.tabView.tabBarHolder.hidden = NO;
    

    //팝업 메뉴 숨기기ƒwe
    //self.menuPopup.alpha = 0.0;
    
//    if (self.strURL == nil) {
//        self.webView = nil;
//        self.webView = [self.oModel mainAkPlazaWebView];
//    }
//    else 
//    {
//        if ([strViewType isEqualToString:kMapType]) {   
//        }
//        else if ([strViewType isEqualToString:kBarcodeType]) {
//        }
//        strViewType = nil;
//        self.webView = [self.oModel mainSubWebView :self.strURL];
//        self.strURL = nil;
//    }
    
    self.webView.frame = self.contentView.frame;
    
//    self.progressView.frame = CGRectMake(0, 0, 320, 8);
//    self.progressView.progress = 0.0f;
//    [self.progressView setAlpha:0.6f];
//    [self.progressView setTintColor:kFontBrownColor];
//    [self.view addSubview:self.progressView];
    
//    self.webView.progressDelegate = self;
    
    //[(UIScrollView *)[[self.webView subviews] lastObject] setScrollsToTop:NO];
    

    
    

    
    //[oModel release];
    //oModel = nil;
}


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];

    self.oModel = [[AkMainModel alloc] init];
    
    self.oModel.delegate = self;
    
    //20150825 MINSEOK cookie always
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookieAcceptPolicy:NSHTTPCookieAcceptPolicyAlways];
    
    //버전 체크 
    
    [self.oModel procVersion];
    
    
    self.strURL = nil;
    
    self.tabView = [[GlobalValues sharedSingleton] tabBar];
    
//    self.progressView = [[PDColoredProgressView alloc] initWithProgressViewStyle: UIProgressViewStyleDefault];
    
    if(self.webView == nil){
        self.webView = [self.oModel mainAkPlazaWebView];
        self.webView.frame = self.contentView.frame;
        [self.contentView addSubview:self.webView];

    }
    self.webView = [self.oModel mainAkPlazaWebView];
    self.webView.frame = self.contentView.frame;
    
    [self.webView setBackgroundColor:[UIColor whiteColor]];
    [self.contentView addSubview:self.webView];
 
    if(isPhotoBack){
        DLog(@"isPhotoBack");
        isPhotoBack = NO;
        [self.webView reload];
        return;
    }else{
        
        
        //[[GlobalValues sharedSingleton] setPushUrl:@"http://m.akplaza.com/main.do"];
        
        NSLog(@" [GlobalValues sharedSingleton] returnUrl] %@", [[GlobalValues sharedSingleton] returnUrl]);
                NSLog(@" [GlobalValues sharedSingleton] pushUrl] %@", [[GlobalValues sharedSingleton] pushUrl]);
                NSLog(@" [GlobalValues sharedSingleton] kAddressURL] %@",kAddressURL);


        if(self.strURL == nil){
            if([[GlobalValues sharedSingleton] returnUrl] == nil){//p65458 2013.07.11  returnurl add
                if([[GlobalValues sharedSingleton] pushUrl] == nil){//p65458 2013.08.08  push 페이지 이동 add
                    
                    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@?act=index", kAddressURL ]]]];
                }else{
                    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[[GlobalValues sharedSingleton] pushUrl]]]];
                    [[GlobalValues sharedSingleton] setPushUrl:nil];
                }
            }else{
                [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[[GlobalValues sharedSingleton] returnUrl]]]]; //p65458 2013.07.11  returnurl add
                [[GlobalValues sharedSingleton] setReturnUrl:nil];
            }
        }else{
            [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.strURL]]];
        }
    }
    
    [[GlobalValues sharedSingleton] tabBar].webView = self.webView;
    
//    //상태바 배경색
//    UIView *setatusBG = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 320, 20)];
//    [setatusBG setBackgroundColor:[UIColor whiteColor]];
//    [self.view addSubview:setatusBG];
    
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
    
    /*
    act.center = CGPointMake(160, 169);
	
    [self.contentView insertSubview:act atIndex:999];
    
    [act startAnimating];
    */
}


-(void) actionGoBack
{
    
    
    [self.btnLeft setImage:nil forState:UIControlStateNormal];
    [self.btnLeft setImage:nil forState:UIControlStateHighlighted];
    
    
    
    /*
    if (self.webView.canGoBack) {
        [self.btnLeft setImage:nil forState:UIControlStateNormal];
        [self.btnLeft setImage:nil forState:UIControlStateHighlighted];
    }
    else
    {
        
        [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_qr" ofType:@"png"]] forState:UIControlStateNormal];
        [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_qr_s" ofType:@"png"]] forState:UIControlStateHighlighted];  
        
        
    }
     */
}

@end
