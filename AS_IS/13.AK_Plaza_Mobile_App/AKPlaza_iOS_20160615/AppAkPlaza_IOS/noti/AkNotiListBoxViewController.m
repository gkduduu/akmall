//
//  AkNotiListBoxViewController.m
//  AppAkPlaza_iOS
//
//  Created by uniwis on 11. 6. 13..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AkNotiListBoxViewController.h"
#import "AkNotiContentViewController.h"
#import "LoginViewController.h"
#import "GlobalValues.h"
#import "UWTabBarDefine.h"//p65458 20130620 add
#import "AKAppDelegate.h"

#define kPageDivide  5

@implementation AkNotiListBoxViewController


@synthesize tableView,arrNotilist, nPageNum,activityView, oModel,pushid,bc;
@synthesize arrNotiUrl;//p65458 20130620 add

- (void)dealloc
{

    [tableView release];
    [arrNotilist release];
    [activityView release];
    
    
    [oModel release];
    [pushid release];
    
    [bc release];
    
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - event
-(void)doLeftButton:(id)sender
{
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=index", kAddressURL ]];
    
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    UWTabBar *tabBar = [[GlobalValues sharedSingleton] tabBar];
    tabBar.currentIndex = 0;
    [tabBar.webView loadRequest:reqURL];
    [self.navigationController popViewControllerAnimated:YES];
}





-(void)clickMore:(id)sender
{
    //more
    nPageNum++;
    
    
    int nTotalCnt = [self.arrNotilist count];
    
    if (nTotalCnt > kPageDivide) 
    {
        
        
        [self.tableView reloadData];
        
        
        //더보기 할때 테이블뷰 스크롤의 포지션이 아래에서 위로 올라가게끔 .. 
        
        
        int currRow;
        
        
        if ((nPageNum * kPageDivide) < nTotalCnt)
        {
            
            currRow = (nPageNum * kPageDivide);
            
        }
        else
        {
            currRow = [self.arrNotilist count];
        }
        
        
        
        NSIndexPath *idxPath = [NSIndexPath indexPathForRow:currRow-1 inSection:0];
        
        
        
        
        [self.tableView scrollToRowAtIndexPath:idxPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
    }
}


#pragma mark - View lifecycle
-(void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    //self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation_web" owner:self options:NULL] lastObject];//p65458 20130620 add

//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    
    nPageNum = 1;
    
    
    self.oModel = [[AkNotiModel alloc] init];
    
    self.oModel.delegate = self;
    
    /*
    NSString *path = [[NSBundle mainBundle] bundlePath];
    NSString *finalPath = [path stringByAppendingPathComponent:@"testNotiData.plist"];
    NSDictionary *outlineData = [[NSDictionary dictionaryWithContentsOfFile:finalPath] retain];
    
    self.arrNotilist = [outlineData objectForKey:@"NotiList"];
    
    [outlineData release];
     */

    
    UIView *titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 35)];
    titleView.backgroundColor = kTitleBgColorBrown;
    
    CALayer *bottom = [CALayer layer];
    bottom.backgroundColor = kTitleBorderColor.CGColor;
    bottom.frame = CGRectMake(0, titleView.frame.size.height-kTitleBorderWidth, titleView.frame.size.width, kTitleBorderWidth);
    [titleView.layer addSublayer:bottom];


    UIImageView *titleBuletImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"title_bulet" ofType:@"png"]]];

    titleBuletImg.frame = CGRectMake(10, 9, 8, 17);
    
    [titleView addSubview:titleBuletImg];
    
    UILabel *lblTitle = [[UILabel alloc] initWithFrame:CGRectMake(19, 9, 291, 17)];
    lblTitle.backgroundColor = [UIColor clearColor];
    lblTitle.text = @"알림보관함";
    lblTitle.textColor = kFontTitleColor;
    lblTitle.textAlignment = UITextAlignmentLeft;
    lblTitle.font = kFontSizeTitle;
    
    [titleView addSubview:lblTitle];
    
    [self.contentView addSubview:titleView];
    
    [lblTitle release];
    lblTitle = nil;
    [titleView release];
    titleView = nil;
    [titleView release];
    titleView = nil;

    CGFloat height = [[UIScreen mainScreen]bounds].size.height;
    CGRect tableViewFrame;
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7) {
        tableViewFrame = CGRectMake(0.0, 35.0, 320, height-35-20-70-46-40);
    } else {
        tableViewFrame = CGRectMake(0.0, 35.0, 320, height-35-70-46-40);
    }
    
//    2013.11.14 changuk 수정
//    CGRect tableViewFrame = CGRectMake(0.0, 35.0, 320, 288);
    self.tableView = [[UITableView alloc ] initWithFrame:tableViewFrame style:UITableViewStylePlain];
    self.tableView.delegate = self;
    self.tableView.dataSource = self; 
    self.tableView.opaque = NO;
    self.tableView.backgroundColor = kBgColor;
    [self.tableView setSeparatorColor:kTableViewSeparatorColor];
    if ([self.tableView respondsToSelector:@selector(setSeparatorInset:)]) {
        [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    [self.contentView addSubview:self.tableView];
    [self.tableView reloadData];
    
  
    //더보기부분 제외 2014.10.29오창욱
//    UIButton *moreImg = [UIButton buttonWithType:UIButtonTypeCustom];
//    
//    [moreImg setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_more" ofType:@"png"]] forState:UIControlStateNormal];
//    
//    [moreImg setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_more" ofType:@"png"]] forState:UIControlStateHighlighted];
//    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7) {
//        moreImg.frame = CGRectMake(0, height-20-70-46-49-40, 320, 48);
//    } else {
//        moreImg.frame = CGRectMake(0, height-70-46-49-40, 320, 48);
//    }
//    2013.11.14 changuk 수정
//    moreImg.frame = CGRectMake(0, 313, 320, 48);
    
//    [moreImg addTarget:self action:@selector(clickMore:) forControlEvents:UIControlEventTouchUpInside];
    
//    [self.contentView addSubview:moreImg];
 
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    self.tableView = nil;
    self.arrNotilist = nil;
    self.activityView = nil;
    
    
    self.oModel = nil;
    self.pushid = nil;
    self.bc = nil;
    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    //푸시 서비스 할때 배지값을 0으로 초기화한다.
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    [[[GlobalValues sharedSingleton] tabBar] setBadge:[GlobalValues getNotice]];
    
    
    self.arrNotilist = [[[NSMutableArray alloc] initWithArray:[self.oModel performSelector:@selector(notiList)]] autorelease];
    
    [self.oModel activityStop];
    
    [self.tableView reloadData];
    
    
    OLogin *oLogin = [OLogin loadOLoginContextFromUserDefaults];
    
    
    
    self.pushid = [[NSUserDefaults standardUserDefaults] objectForKey:@"PID"];
    DLog(@"pushid : %@", self.pushid);
    self.bc = [[NSUserDefaults standardUserDefaults] objectForKey:@"BC"];
    DLog(@"bc : %@", self.bc);

    if (self.pushid && [self.pushid length] > 0) 
    {
    
        //전지점 알림이면..(2012.04.20 이후에는 각 지점이든 전지점알림이든 bc가 99이게 'AKAppDelegate'에다 설정해놨다. 그래서 else구문은 사용안됨
        if ([self.bc isEqualToString:@"99"] || oLogin.isLogin) 
        {
            
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];
            
            
                
                for (NSMutableDictionary* dicParam in self.arrNotilist) {
                    
                    NSString* strPushID = [NSString stringWithString: [dicParam objectForKey:@"PUSH_ID"]];
                    
                    if ([self.pushid isEqualToString:strPushID]) {
                        
                        AkNotiContentViewController *akNoti = [[[AkNotiContentViewController alloc] init] autorelease];
                        
                        akNoti.arrNotiContent = dicParam;
                        
                        [self.navigationController pushViewController:akNoti animated:YES];
                        
                        self.pushid = nil;
                        
                        return;
                    }
                    
                    
                }
        

        }
        //각 지점의 알림이면..
        else
        {
            
            
            //로그인 해야함.
            LoginViewController *loginView = [[LoginViewController alloc] init];
            
            loginView.isLeftBtn = YES;
            
            [self.navigationController pushViewController:loginView animated:YES];
            
            loginView = nil;
            
            
        }
        
        
    }
    
    
    
    [[GlobalValues sharedSingleton] tabBar].flag = false;
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    //#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //#warning Incomplete method implementation.
    // Return the number of rows in the section.
    
//    int nTotalCnt = [self.arrNotilist count];
//    
//    if ((nPageNum * kPageDivide) < nTotalCnt)
//    {
//        
//        return (nPageNum * kPageDivide);
//        
//    }
//    else
//    {
//        return [self.arrNotilist count];
//    }
    
    //더보기 기능 제외로 수정 2014.10.29오창욱
    return [arrNotilist count];
    
}




-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
//    UIImage *image = nil;
//    UIImage *image_s = nil;
//    
//    image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_bg" ofType:@"png"]];
//    image_s = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_bg_s" ofType:@"png"]];
    
//    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
//    UIImageView* imgView_s = [[UIImageView alloc] initWithImage:image_s];
    
//    cell.backgroundView = imgView;
//    cell.selectedBackgroundView = imgView_s;
    
//    [imgView release];
//    [imgView_s release];
    
    //selected 됬을때... 
    /*
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    

    
    
    cell.textLabel.textColor  = kFontDarkBrownColor;
    cell.detailTextLabel.textColor  = kFontGrayColor;
    
    cell.detailTextLabel.highlightedTextColor  = kFontGrayColor;
    cell.textLabel.highlightedTextColor  = kFontDarkBrownColor;

    
    cell.textLabel.font = kFontSizeLarge;
    cell.detailTextLabel.font = kFontSizeSub;
     */
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 56;
}



#pragma mark -
#pragma mark Configuring table view cells

#define ROW_HEIGHT 56

#define NAME_TAG 1
#define TIME_TAG 2
#define IMAGE_TAG 3

#define LEFT_COLUMN_OFFSET 10.0
#define LEFT_COLUMN_WIDTH 210.0

#define MIDDLE_COLUMN_OFFSET 225.0
#define MIDDLE_COLUMN_WIDTH 70.0

#define RIGHT_COLUMN_OFFSET 300.0

#define MAIN_FONT_SIZE 18.0

#define LABEL_HEIGHT 50.0

#define IMAGE_SIDE_w 11.0
#define IMAGE_SIDE_h 18.0

- (UITableViewCell *)tableViewCellWithReuseIdentifier:(NSString *)identifier {
	
	/*
	 Create an instance of UITableViewCell and add tagged subviews for the name, local time, and quarter image of the time zone.
	 */
    
	UITableViewCell *cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier] autorelease];
    
	/*
	 Create labels for the text fields; set the highlight color so that when the cell is selected it changes appropriately.
     */
	UILabel *label;
	CGRect rect;
	
	// Create a label for the time zone name.
	rect = CGRectMake(LEFT_COLUMN_OFFSET, (ROW_HEIGHT - LABEL_HEIGHT) / 2.0, LEFT_COLUMN_WIDTH, LABEL_HEIGHT);
	label = [[UILabel alloc] initWithFrame:rect];
	label.tag = NAME_TAG;
	label.font = kFontSizeLarge;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
	label.adjustsFontSizeToFitWidth = NO;
	[cell.contentView addSubview:label];
    label.textColor = kFontDarkBrownColor;
	label.highlightedTextColor = kFontDarkBrownColor;
	[label release];
	
	// Create a label for the time.
	rect = CGRectMake(MIDDLE_COLUMN_OFFSET, (ROW_HEIGHT - LABEL_HEIGHT) / 2.0, MIDDLE_COLUMN_WIDTH, LABEL_HEIGHT);
	label = [[UILabel alloc] initWithFrame:rect];
	label.tag = TIME_TAG;
	label.font = kFontSizeSub;
	label.textAlignment = UITextAlignmentRight;
    label.backgroundColor = [UIColor clearColor];
	[cell.contentView addSubview:label];
    label.textColor = kFontAccSaColor;
	label.highlightedTextColor = kFontGrayColor;
	[label release];
    
	// Create an image view for the quarter image.
	rect = CGRectMake(RIGHT_COLUMN_OFFSET, (ROW_HEIGHT - IMAGE_SIDE_h) / 2.0, IMAGE_SIDE_w, IMAGE_SIDE_h);
    
	UIImageView *imageView = [[UIImageView alloc] initWithFrame:rect];
	imageView.tag = IMAGE_TAG;
	[cell.contentView addSubview:imageView];
	[imageView release];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
	
	return cell;
}


- (void)configureCell:(UITableViewCell *)cell forIndexPath:(NSIndexPath *)indexPath {
    
    
    NSMutableDictionary *items = [[[NSMutableDictionary alloc] initWithDictionary:[self.arrNotilist objectAtIndex:indexPath.row]] autorelease];
    
    
    UILabel *label;
	
	// Set the locale name.
	label = (UILabel *)[cell viewWithTag:NAME_TAG];
    
    label.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]];
    
    NSString *Read = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:7]];
    if([Read isEqual:@"1"]){
        [label setTextColor:AlarmColor];
    } else {
        [label setTextColor:[UIColor blackColor]];
    }
    
    
    label = (UILabel *)[cell viewWithTag:TIME_TAG];
    label.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:4]];
    
    
    //액세서리 뷰 이미지 바꾸기~ 
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow" ofType:@"png"]];
    
    UIImageView *imageView = (UIImageView *)[cell viewWithTag:IMAGE_TAG];
    
    imageView.image = image;
    
    //}
    
    items = nil;
    image = nil;

    
    
    
   
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
        
        if (buttonIndex == 1) {
            
            //Web URL View
//            CGFloat height = [[UIScreen mainScreen]applicationFrame].size.height;
//            
//            int frameY = 0;
//            
//            if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7) {
//                frameY = 20;
//            }
//            
//            UIWebView *webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, frameY , 320, height-46)];
//            2013.11.14 changuk 수정
//            UIWebView *webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, 320, 440)];
            //[webView setDelegate:self];
//            [webView setDelegate:(id<UIWebViewDelegate>)self];
            
            NSString *url = [NSString stringWithString:[self.arrNotiUrl objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:3]]];
            
            NSURLRequest *reqURL = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
            UWTabBar *tabBar = [[GlobalValues sharedSingleton] tabBar];
            tabBar.currentIndex = 0;
            [tabBar.webView loadRequest:reqURL];
            [self.navigationController popViewControllerAnimated:YES];
            

            
//            [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
//            [self.view addSubview:webView];
//            [webView release];
            
            /*
            //탭바 home로 이동
            UITabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
            
            
            [uwTabBar selectedItem: [[uwTabBar tabItemsArray ] objectAtIndex:0]];
            */
            //NSString *strUrl = [NSString stringWithFormat:@"%@/app/versionIOS.jsp", kRootURL];
            /*
            NSString *strUrl= @"www.daum.net";
            
            UIWebView *webView = [[UIWebView alloc] init];
            
            NSURL *url = [NSURL URLWithString:strUrl];
            NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
            
            webView.scalesPageToFit = YES;
            //webView.multipleTouchEnabled = YES;
            
            [webView loadRequest:reqURL];
            
            [webView setDelegate:self];
            
            
            [webView autorelease];
             */

            /* //p65458 20130620 알림 보관함 이동 
            AkNotiListBoxViewController *subView = [[AkNotiListBoxViewController alloc] init];
            
            [self.navigationController pushViewController:subView animated:YES];
            
            subView = nil;
            */ //p65458 20130620 알림 보관함 이동
            
            /*//p645458 20130620 safari을 구동해서 url 이동으로 block
            // openURL 로 링크 실행
            NSString *strURL = [NSString stringWithFormat:@"%@?act=akmall", kAddressURL];
            
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:strURL]];
            *///p645458 20130603 safari을 구동해서 url 이동으로 block
        }

}




- (UITableViewCell *)tableView:(UITableView *)tView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    /*
    
    UITableViewCell *cell = [tView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier] autorelease];
        
    }
     
    
    
    
    NSMutableDictionary *items = [[[NSMutableDictionary alloc] initWithDictionary:[self.arrNotilist objectAtIndex:indexPath.row]] autorelease];
    
    cell.detailTextLabel.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:4]];
    cell.textLabel.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]];
    cell.textLabel.numberOfLines = 2;
    
    //액세서리 뷰 이미지 바꾸기~ 
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow" ofType:@"png"]];
    
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.accessoryView = imgView;
    
    [imgView release];
    //}
    
    items = nil;
    imgView = nil;
    image = nil;
    
    
    return cell;
     */
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (cell == nil) {
		cell = [self tableViewCellWithReuseIdentifier:CellIdentifier];
	}
	
	// configureCell:cell forIndexPath: sets the text and image for the cell -- the method is factored out as it's also called during minuted-based updates.
	[self configureCell:cell forIndexPath:indexPath];
	return cell;

}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    /*
        
    BOOL isGoNotiContentView = NO;
    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if ((nPageNum * kPageDivide) < [arrNotilist count]) {
        
        if(indexPath.row != (nPageNum * kPageDivide))
        {
            
            isGoNotiContentView = YES;
            
        }
        else
        {
            nPageNum++;
            
            activityIndicator = [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(0, 0, 32, 32)];
            [activityIndicator setCenter:CGPointMake(160, 208)];
            [activityIndicator setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleGray];
            [self.view addSubview:activityIndicator];
            [activityIndicator release];
            [activityIndicator startAnimating];
            //2초 후에 정지하기
            [self performSelector:@selector(stopAnimation) withObject:nil afterDelay:0.5];
            
            
            [self.tableView reloadData];
        }
    }
    else
    {
        isGoNotiContentView = YES;
    }
     */
   
    
    //if (isGoNotiContentView) {
    
    //NSDictionary* item = [self.arrNotilist objectAtIndex:indexPath.row]; //p65458 20130620 block
    
    NSMutableDictionary *items = [[[NSMutableDictionary alloc] initWithDictionary:[self.arrNotilist objectAtIndex:indexPath.row]] autorelease];

    
    //p65458 20130620 기존 시나리오 변경으로 add

    
    //푸시 알림이 오면 보이는 메세지
    //MSG_DELEGATE_BTN2(@"AK플라자",  @"닫기",  @"보기", [item objectForKey:@"NotiName"] , self);
    MSG_DELEGATE_BTN2(@"AK플라자",  @"닫기",  @"보기", [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]] , self);
    
    self.arrNotiUrl = [self.arrNotilist objectAtIndex:indexPath.row];

    
    //p65458 20130620 기존 시나리오 변경으로 add
    
    NSString *userDeviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    if ([userDeviceToken length] > 0) {
    
        //알림체크를 위한로직
        NSURL *url = [NSURL URLWithString:[kLibURL stringByReplacingOccurrencesOfString:@"http://" withString:kHTTP]];
        ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
        
        [request setDelegate:self];
        
        [request setRequestMethod:@"POST"];
        
        [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
        
        [request setPostValue:@"pushDetail"   forKey:@"act"];
        [request setPostValue:[items objectForKey:@"PUSH_ID"]   forKey:@"push_id"];
        [request setPostValue:userDeviceToken   forKey:@"token"];
        
        [request startSynchronous];
        
        [[[GlobalValues sharedSingleton] tabBar] setBadge:[GlobalValues getNotice]];
    }
    
    UITableViewCell* cell = [self.tableView cellForRowAtIndexPath:indexPath];
    UILabel *label = (UILabel *)[cell viewWithTag:NAME_TAG];
    [label setTextColor:AlarmColor];
    
#ifdef p65458//p65458 20130620 기존 시나리오 변경으로 block

    
       presentModalViewController
        
    //akNotiContentViewController.strTitle = [item objectForKey:@"NotiName"];
    //akNotiContentViewController.strShortURL = [item objectForKey:@"NotiURL"];
    
    akNoti.arrNotiContent= [self.arrNotilist objectAtIndex:indexPath.row];
    
        /*
         
         akNotiContentViewController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
         [self presentModalViewController:akNotiContentViewController animated:YES];
         */
        
        [self.navigationController pushViewController:akNoti animated:YES];
    //}
    
#endif//p65458 20130620 기존 시나리오 변경으로 block
    
    
    //item = nil; //p65458 20130620 block
    items = nil;
       
    
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
