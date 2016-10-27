//
//  CartViewController.m
//  test
//
//  Created by 한병일 on 2014. 6. 12..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "CartViewController.h"
#import "AppDelegate.h"

@interface CartViewController ()

@end

@implementation CartViewController
@synthesize webView;
@synthesize activityView;

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
    
    self.activityView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    activityView.center = self.view.center;
    //[activityView setBackgroundColor:[UIColor whiteColor]];
    [activityView setColor:UIColorFromRGB(0XE20167)];
    
    [self.view addSubview:activityView];
    
    [self.webView setDelegate:self];
    [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:MainURL_3]]];
    
    AppDelegate *appDele = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    self.webView.scrollView.delegate = appDele;
}

- (void)webViewDidStartLoad:(UIWebView *)webView{
	self.activityView.hidden = NO;
	[activityView startAnimating];
}


- (void)webViewDidFinishLoad:(UIWebView *)webView {
	self.activityView.hidden = YES;
    [activityView stopAnimating];
    // 결제완료 Tracking
    [IgawTrackingHelper trackingPurchase:self.webView];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
//    파일첨부시 리로드 오류로 인하여 viewDidLoad로 이동
//    [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:MainURL_3]]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(BOOL) webView:(UIWebView *)mwebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication]delegate];
    //20150825 MINSEOK cookie always
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookieAcceptPolicy:NSHTTPCookieAcceptPolicyAlways];
    return [appDelegate webView:mwebView shouldStartLoadWithRequest:request navigationType:navigationType];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        NSString *pStr =  [[NSString stringWithFormat:@"tel:%ld",(long)alertView.tag] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:pStr]];
    }
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
