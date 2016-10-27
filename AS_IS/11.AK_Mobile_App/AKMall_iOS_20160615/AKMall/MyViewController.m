//
//  MyViewController.m
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "MyViewController.h"
#import "AppDelegate.h"

#define tabHeight 52

@interface MyViewController ()

@end

@implementation MyViewController {
    CGFloat startContentOffset;
    CGFloat lastContentOffset;
    BOOL footerHide; // true이면 보임 false이면 안보임
}
//@synthesize webView;
@synthesize activityView;
@synthesize pushid;

#define kNotiDeviceUSERRoot_Item         @"root"
#define kNotiDeviceUSER_Item             @"updateduser"
#define kNotiDeviceUSER_Gubun            @"gubun"
#define NOTI_DEVICEUSER_ITEM_ATTREBUTE [[NSArray alloc] initWithObjects:kNotiDeviceUSERRoot_Item, kNotiDeviceUSER_Item, kNotiDeviceUSER_Gubun, nil]

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.pushid = nil;
    footerHide = true;
    self.activityView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    activityView.center = self.view.center;
    //[activityView setBackgroundColor:[UIColor whiteColor]];
    [activityView setColor:UIColorFromRGB(0XE20167)];
    
    [self.view addSubview:activityView];
    
    [self.webView setDelegate:self];
    if (self.urls == nil) {
        self.urls = MainURL_1;
    }
    [self.webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:self.urls]]];
    [self.hiddenWebView setDelegate:self];
    
//    AppDelegate *appDele = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    self.webView.scrollView.delegate = self;
    
}

- (void)webViewDidStartLoad:(UIWebView *)webView{
	self.activityView.hidden = NO;
	[activityView startAnimating];
}

- (void)webViewDidFinishLoad:(UIWebView *)webview {
//    if( webview.tag == 1)
//    {
//        [self.webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:MainURL_5]]];
//    }
    
	self.activityView.hidden = YES;
	[activityView stopAnimating];
    
    // 결제완료 Tracking
    [IgawTrackingHelper trackingPurchase:self.webView];
    
//    if ( ![[[NSUserDefaults standardUserDefaults] stringForKey:@"PID"]  isEqual: @""])
//    {
//        self.pushid = [[NSUserDefaults standardUserDefaults] stringForKey:@"PID"];
//        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:@"PID"];
//        [[NSUserDefaults standardUserDefaults] synchronize];
//    }
    
//    if(self.pushid != nil)
//    {
//        [self GoNotiBox];
//    }
    

//    어플 실행시에만 updateDeviceUser 호출하도록수정 2014.10.06오창욱
//    NSString *req = webview.request.URL.absoluteString;
//    
//    NSRange actionLoginProc = [req rangeOfString:@"LoginProc.do"];
//    if( actionLoginProc.location != NSNotFound)
//    {
//        NSString *loginedID = [[NSUserDefaults standardUserDefaults] stringForKey:@"LoginID"];
//        [self updateDeviceUser:loginedID];
//    }
    
    footerHide = [[self.webView stringByEvaluatingJavaScriptFromString:@"appDisplayFooter"] length] != 0?true:false;
    [self tabHide:footerHide];
    
}

- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:animated];

    [super viewWillAppear:animated];
}

- (void)viewWillLayoutSubviews
{
    self.view.frame = self.navigationController.view.frame;
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    
    
//    NSString *req = self.webView.request.URL.absoluteString;
    
//    NSLog(@"%@",self.naviString);

//    if(self.naviString == nil) {
//        if( [req rangeOfString:@"/mypage/UserSetupMain.do"].location == NSNotFound)
//        {
////            [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:MainURL_5]]];
//        }else if (req == nil)
//        {
//            [self.webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:MainURL_1]]];
//        }
//    } else {
//        [self.webView loadRequest:self.naviString];
//        self.naviString = nil;
//    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(BOOL) webView:(UIWebView *)mwebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    
    
    NSString *req = [[request URL] absoluteString];
    
//    NSLog(@"%@",req);
    
    if ([req isEqualToString:@"about:blank"]) {
        return false;
    }
    
    if([[[request URL] scheme] isEqualToString:@"akmall"])
    {
        NSRange actionLogout = [req rangeOfString:@"Logout.do"];
        if(actionLogout.location != NSNotFound)
        {
            [self.hiddenWebView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:MembersLogout]]];
        }
        
        NSRange actionNotiSetting = [req rangeOfString:@"act=viewNotiSetting"];
        if(actionNotiSetting.location != NSNotFound)
        {
            
            [self GoSettingView];
            
        }
        
        NSRange actionNotiBox = [req rangeOfString:@"act=viewNotiBox"];
        if(actionNotiBox.location != NSNotFound)
        {
            
            
            [self GoNotiBox];
            
            
        }
    }
    
    BOOL headerIsPresent = [[request allHTTPHeaderFields] objectForKey:@"makm-device"]!=nil;
    NSRange actionSetup = [req rangeOfString:@"UserSetupMain.do"];
    
    if(actionSetup.location != NSNotFound)
    {
//    어플 실행시에만 updateDeviceUser 호출하도록수정 2014.10.06오창욱
//        NSString *loginedID = [[NSUserDefaults standardUserDefaults] stringForKey:@"LoginID"];
//        [self updateDeviceUser:loginedID];
        
        if(!headerIsPresent) {
            NSMutableURLRequest *personalRequest = (NSMutableURLRequest *)request;
            if ([personalRequest respondsToSelector:@selector(setValue:forHTTPHeaderField:)])
            {
                [personalRequest setValue:@"iOS" forHTTPHeaderField:@"makm-device"];
                [personalRequest setValue:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"] forHTTPHeaderField:@"makm-version"];
            }
            
            [self.webView loadRequest:request];
            return NO;
        }
    }
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication]delegate];
    //20150825 MINSEOK cookie always
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookieAcceptPolicy:NSHTTPCookieAcceptPolicyAlways];
    return  [appDelegate webView:mwebView shouldStartLoadWithRequest:request navigationType:navigationType];
}

-(void)GoNotiBox
{
    [self performSegueWithIdentifier: @"NotiBox" sender: self];
}

-(void)GoSettingView
{
    [self performSegueWithIdentifier: @"Setting" sender: self];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        NSString *pStr =  [[NSString stringWithFormat:@"tel:%ld",(long)alertView.tag] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:pStr]];
    }
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"NotiBox"]) {
        NotiBoxTableViewController *view = [segue destinationViewController];
        view.delegate = self;
        if( self.pushid != nil)
        {
            view.pushid = self.pushid;
            self.pushid = nil;
        }
    }
}

- (NSNumber*)updateDeviceUser:(NSString*)userID
{
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSString *strURL = [NSString stringWithFormat:@"%@?act=updateDeviceUser&phonetype=0&token=%@&userid=%@&version=%@", LibURL, deviceToken, userID ,[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTI_DEVICEUSER_ITEM_ATTREBUTE andWithParserType:(ParserType *)Parser1];
    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSString *gubun = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:kNotiDeviceUSER_Gubun];
        
//        NSLog(@"updateDeviceUser_Gubun : %@", gubun);
        
        if ([gubun isEqualToString:@"COMPLETE"]) {
            return [[NSNumber alloc] initWithBool:YES];
        }
        
    }
    
    return [[NSNumber alloc] initWithBool:NO];
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    startContentOffset = lastContentOffset = scrollView.contentOffset.y;
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    CGFloat currentOffset = scrollView.contentOffset.y;
    //    CGFloat differenceFromStart = startContentOffset - currentOffset;
    CGFloat differenceFromLast = lastContentOffset - currentOffset;
    lastContentOffset = currentOffset;
    
    if (footerHide) {
        CGFloat height = self.view.frame.size.height;
        if(scrollView.isTracking && fabs(differenceFromLast)>1) {
            
            
            CGRect frame = self.webView.frame;
            CGRect frame2 = self.bottomTab.frame;
            
            frame.size.height -= differenceFromLast;
            frame2.origin.y -= differenceFromLast;
            
            if (frame2.origin.y < height-tabHeight) {
                frame.size.height = height-tabHeight -20;
                frame2.origin.y = height-tabHeight;
            } else if (frame2.origin.y > height) {
                frame.size.height = height -20;
                frame2.origin.y = height;
            }
            
            self.webView.frame = frame;
            self.bottomTab.frame = frame2;
            
        }
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    if (footerHide) {
        CGFloat height = self.view.frame.size.height - tabHeight / 2;
        [self tabHide:height >= self.bottomTab.frame.origin.y];
    }
}

- (void)tabHide:(BOOL)hide
{
    [UIView animateWithDuration:0.2 animations:^{
        CGRect frame = self.bottomTab.frame;
        CGRect frame2 = self.webView.frame;
        if (hide) {
            frame.origin.y = self.view.frame.size.height - tabHeight;
            frame2.size.height = self.view.frame.size.height -tabHeight -20;
        } else {
            frame.origin.y = self.view.frame.size.height;
            frame2.size.height = self.view.frame.size.height - 20;
        }
        self.bottomTab.frame = frame;
        self.webView.frame = frame2;
    }];
}


- (BOOL)scrollViewShouldScrollToTop:(UIScrollView *)scrollView
{
    if (footerHide) {
        [UIView animateWithDuration:0.2 animations:^{
            CGRect frame = self.bottomTab.frame;
            CGRect frame2 = self.webView.frame;
            
            frame.origin.y = self.view.frame.size.height - tabHeight;
            frame2.size.height = self.view.frame.size.height -tabHeight -20;
            
            self.bottomTab.frame = frame;
            self.webView.frame = frame2;
        }];
    }
    return YES;
}

-(void)GoUrl:(NSString*)URL
{
    NSLog(@"GoUrl:(NSString*)%@",URL);
    [self.webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:URL]]];
}

- (IBAction)tabToggle:(UIButton *)sender {
    
    for (int i = 51; i<=56; i++) {
        UIButton *btn = [self.view viewWithTag:i];
        btn.selected = (i == sender.tag);
    }
    
    if (sender.tag == 51) {
        [self.webView stringByEvaluatingJavaScriptFromString:@"clickTab(1)"];
    } else if (sender.tag == 52) {
        [self.webView stringByEvaluatingJavaScriptFromString:@"clickTab(2)"];
    } else if (sender.tag == 53) {
        [self.webView stringByEvaluatingJavaScriptFromString:@"clickTab(3)"];
    } else if (sender.tag == 54) {
        [self.webView stringByEvaluatingJavaScriptFromString:@"clickTab(4)"];
    } else if (sender.tag == 55) {
        [self.webView stringByEvaluatingJavaScriptFromString:@"clickTab(5)"];
    } else if (sender.tag == 56) {
        [self.webView stringByEvaluatingJavaScriptFromString:@"clickTab(6)"];
    }
}

@end
