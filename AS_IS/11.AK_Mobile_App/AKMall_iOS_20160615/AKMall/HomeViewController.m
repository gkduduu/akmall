//
//  HomeViewController.m
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "HomeViewController.h"
#import "AppDelegate.h"

#import "AppDelegate.h"

@interface HomeViewController ()

@end

@implementation HomeViewController
{
    CGFloat startContentOffset;
    CGFloat lastContentOffset;
}

@synthesize webView;
@synthesize menualView;
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
    [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:self.urls]]];
    
    self.webView.scrollView.delegate = self;

//    self.originalFrame = self.tabBarController.tabBar.frame;
//    self.webViewOriginalFrame = self.webView.frame;
    
}

- (void)webViewDidStartLoad:(UIWebView *)webView{
	self.activityView.hidden = NO;
	[activityView startAnimating];
}


- (void)webViewDidFinishLoad:(UIWebView *)mWebView {
	self.activityView.hidden = YES;
	[activityView stopAnimating];
    // 결제완료 Tracking
    [IgawTrackingHelper trackingPurchase:self.webView];
//    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication]delegate];
//    [appDelegate webViewDidFinishLoad:mWebView];
    
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    
//    파일첨부시 리로드 오류로 인하여 viewDidLoad로 이동
//    [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:self.urls]]];
//    self.urls = MainURL_1;
}

-(void)GoUrl:(NSString*)URL
{
    [webView loadRequest:[[NSURLRequest alloc]initWithURL:[NSURL URLWithString:URL]]];
}

- (IBAction)tabToggle:(UIButton *)sender {
    
    for (int i = 51; i<=56; i++) {
        UIButton *btn = [self.view viewWithTag:i];
        btn.selected = (i == sender.tag);
    }
    
    if (sender.tag == 51) {
        [self GoUrl:MainURL_1];
    } else if (sender.tag == 52) {
        [self GoUrl:MainURL_2];
    } else if (sender.tag == 53) {
        [self GoUrl:MainURL_7];
    } else if (sender.tag == 54) {
        [self GoUrl:MainURL_3];
    } else if (sender.tag == 55) {
        [self GoUrl:MainURL_4];
    } else if (sender.tag == 56) {
        [self GoUrl:MainURL_5];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    
    
    
    CGFloat height = self.view.frame.size.height;
    
    if(scrollView.isTracking && fabs(differenceFromLast)>1) {
        
            
        CGRect frame = self.webView.frame;
        CGRect frame2 = self.bottomTab.frame;

        frame.size.height -= differenceFromLast;
        frame2.origin.y -= differenceFromLast;
        
        if (frame2.origin.y < height-44) {
            frame.size.height = height-44 -20;
            frame2.origin.y = height-44;
        } else if (frame2.origin.y > height) {
            frame.size.height = height -20;
            frame2.origin.y = height;
        }
        
        self.webView.frame = frame;
        self.bottomTab.frame = frame2;
        
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    CGFloat height = self.view.frame.size.height - 44 / 2;
    [UIView animateWithDuration:0.2 animations:^{
        CGRect frame = self.bottomTab.frame;
        CGRect frame2 = self.webView.frame;
        if (height >= self.bottomTab.frame.origin.y) {
            frame.origin.y = self.view.frame.size.height - 44;
            frame2.size.height = self.view.frame.size.height -44 -20;
        } else {
            frame.origin.y = self.view.frame.size.height;
            frame2.size.height = self.view.frame.size.height - 20;
        }
        self.bottomTab.frame = frame;
        self.webView.frame = frame2;
    }];
    
}


@end
