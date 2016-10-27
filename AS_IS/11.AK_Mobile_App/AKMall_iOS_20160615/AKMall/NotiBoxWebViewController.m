//
//  NotiBoxWebViewController.m
//  AKMall
//
//  Created by 한병일 on 2014. 6. 21..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "NotiBoxWebViewController.h"
#import "AppDelegate.h"

@interface NotiBoxWebViewController ()

@end

@implementation NotiBoxWebViewController
@synthesize URL;
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
    
    AppDelegate *appDele = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    self.webView.scrollView.delegate = appDele;
    
    [self.view addSubview:activityView];
    
    [self.webView setDelegate:self];
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.text = @"          알림 보관함";
    //titleLabel.textAlignment = NSTextAlignmentCenter;
    [self.navigationItem setTitleView:titleLabel];
    
    [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"topbg.png"] forBarMetrics:UIBarMetricsDefault];
    
    UIImage *backImage = [UIImage imageNamed:@"btn_back.png"];
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    backButton.frame = CGRectMake(0, 0, backImage.size.width, backImage.size.height);
    
    [backButton setImage:backImage forState:UIControlStateNormal];
    UIBarButtonItem *backBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [backButton addTarget:self action:@selector(pushBackButton:)    forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.leftBarButtonItem = backBarButtonItem;
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

-(void) pushBackButton:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
    if( URL != nil)
    {
        [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:URL]]];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(BOOL) webView:(UIWebView *)mwebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication]delegate];
    return [appDelegate webView:mwebView shouldStartLoadWithRequest:request navigationType:navigationType];
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
