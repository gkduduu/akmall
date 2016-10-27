 //
//  AkBarcodeHistoryDetailView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 27..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkBarcodeHistoryDetailView.h"

@implementation AkBarcodeHistoryDetailView


@synthesize activityView;
@synthesize webView;
@synthesize strURL;



#pragma mark - View lifecycle end
-(void)dealloc
{
    [activityView release];
    [webView release];
    [strURL release];

    [super dealloc];
}

- (void)viewDidUnload
{
    self.activityView = nil;
    self.webView = nil;
    self.strURL = nil;
    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

#pragma mark - event
-(void)doLeftButton:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}



#pragma mark - View lifecycle start

- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
}


- (void)BarcodeWebview
{
    AkMainModel *oModel = [[AkMainModel alloc] init];
    
    oModel.delegate = self;
    
    
    
    //테스트
    CGRect frame = CGRectMake(0.0, 10.0, self.view.bounds.size.width, self.view.bounds.size.height+20);
    
    self.webView = [oModel mainSubWebView:self.strURL];
    
    self.webView.frame = frame;
    
    [self.contentView addSubview:self.webView];
    //p65458 20150520 webview 와 header 겹치는 문제 발생 ak의 요청으로 앱의 header의 삭제하는 것으로 진행
    
    self.contentView.frame = frame;
    
    self.header.hidden = YES;
    self.naviTitle.hidden = YES;
    //p65458 20150520 webview 와 header 겹치는 문제 발생 ak의 요청으로 앱의 header의 삭제하는 것으로 진행
    
    //self.view.hidden = YES;
}


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    
    UIView *titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 35)];
    titleView.backgroundColor = kTitleBgColorBrown;
    
    CALayer *bottom = [CALayer layer];
    bottom.backgroundColor = kTitleBorderColor.CGColor;
    bottom.frame = CGRectMake(0, titleView.frame.size.height-kTitleBorderWidth, titleView.frame.size.width, kTitleBorderWidth);
    [titleView.layer addSublayer:bottom];
    
    
    UIImageView *titleBuletImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"title_bulet" ofType:@"png"]]];
    
    titleBuletImg.frame = CGRectMake(10, 9, 8, 17);
    
    [titleView addSubview:titleBuletImg];
    
    [self performSelectorOnMainThread:@selector(BarcodeWebview) withObject:nil waitUntilDone:YES];
    
    UILabel *lblTitle = [[UILabel alloc] initWithFrame:CGRectMake(19, 9, 291, 17)];
    lblTitle.backgroundColor = [UIColor clearColor];
    lblTitle.text = self.strURL;
    lblTitle.textColor = kFontTitleColor;
    lblTitle.textAlignment = UITextAlignmentLeft;
    lblTitle.font = [UIFont systemFontOfSize:15];
    
    [titleView addSubview:lblTitle];
    
    //[self.contentView addSubview:titleView];//p65458 20150520 webview 와 header 겹치는 문제 발생 ak의 요청으로 앱의 header의 삭제하는 것으로 진행 s
    
    [lblTitle release];
    lblTitle = nil;
    [titleView release];
    titleView = nil;
    [titleView release];
    titleView = nil;
    
    
   

    
    //[oModel release];
    //oModel = nil;

    
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
