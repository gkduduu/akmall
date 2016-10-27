//
//  AkBarcodeHistoryDetailView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 27..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkBarcodeDetailView.h"
#import "AKAppDelegate.h"

// changuk 2013.11.14 add 아이폰5 대응 이미지 매크로
#define ASSET_BY_SCREEN_HEIGHT(regular, longScreen) ((    [[UIScreen mainScreen] bounds].size.height) == 480 ? regular:longScreen)

@implementation AkBarcodeDetailView


@synthesize activityView;
@synthesize webView;
@synthesize strURL;
@synthesize tabView;
@synthesize barcodeTimer;
@synthesize reader;//p65458 20150511 ios8 not action -> add



#pragma mark - View lifecycle end
-(void)dealloc
{
    [activityView release];
    [webView release];
    [strURL release];
    [tabView release];
    [barcodeTimer release];

    
    [super dealloc];
}

- (void)viewDidUnload
{
    self.activityView = nil;
    self.webView = nil;
    self.strURL = nil;
    self.tabView = nil;
    self.barcodeTimer = nil;
    
    
    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

#pragma mark - event

-(void)doLeftButton:(id)sender
{
    
    [self.navigationController popViewControllerAnimated:YES];
}
 

-(void)doBarcodeLeftButton:(id)sender
{
    
    [self.barcodeTimer invalidate];

    
    //[self dismissModalViewControllerAnimated:YES];//p65458 20150511 ios8 not action -> block

    [self.reader dismissModalViewControllerAnimated:YES];//p65458 20150511 ios8 not action -> add
    //[self presentViewController:widController animated:YES completion:nil];

    
    //뒤로 갈때 새로고침하기
    [self.navigationController viewWillAppear:YES];
    [self.navigationController popViewControllerAnimated:YES];

    

}

-(void)clickSubBarcodeHistory:(id)sender
{
    
    [self.barcodeTimer invalidate];
    
    //[self dismissModalViewControllerAnimated:YES];//p65458 ios8 not action -> block
    [self.reader dismissModalViewControllerAnimated:YES];//p65458 20150511 ios8 not action -> add
    //탭바 My로 이동
    UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
    
    
    //MY 메인의 서브 뷰일때 푸쉬가 오면 루트로 옮긴 다음에 다시
    //현재 탭의 view랑 4번 탭의 뷰가 같으냐?
    
    //루트 네비 뷰에서 네비 얻어오기
    UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];

    
    NSLog(@"Ended %@", self.navigationController);
    
    AkBarcodeHistoryListView *subView = [[AkBarcodeHistoryListView alloc] init];
    
    [navi pushViewController:subView animated:YES];
    
    subView = nil;
    
}


#pragma mark - View lifecycle start

- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation_web" owner:self options:NULL] lastObject];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    self.tabView.tabBarHolder.hidden = NO;
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (self.strURL == nil || [self.strURL length] <= 0) 
    {
        
        [self.navigationController popViewControllerAnimated:YES];
    }
    else
    {
    
        /*
        UIView *titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 35)];
        titleView.backgroundColor = kTitleBgColorBrown;
        
        
        UIImageView *titleBuletImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"title_bulet" ofType:@"png"]]];
        
        titleBuletImg.frame = CGRectMake(10, 9, 8, 17);
        
        [titleView addSubview:titleBuletImg];
        
        UILabel *lblTitle = [[UILabel alloc] initWithFrame:CGRectMake(19, 9, 291, 17)];
        lblTitle.backgroundColor = [UIColor clearColor];
        lblTitle.text = self.strURL;
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

        
        
         */
        
        
        //NSManaged////////////////////////////////////////////////////////////////////////////////
        
        NSError *error;
        
        //바코드 찍은 날짜
        NSDate *createDate = [NSDate dateWithTimeIntervalSinceNow:60];
        
        
        AKAppDelegate *appDelegate = (AKAppDelegate *)[[UIApplication sharedApplication] delegate];
        NSManagedObjectContext *managedObjectContext = appDelegate.managedObjectContext;
        
        
        NSEntityDescription *entityDescription = [NSEntityDescription entityForName:@"QRCodeHistoryTable" inManagedObjectContext:managedObjectContext];
        
        
        NSManagedObject *managedData = [[NSManagedObject alloc] initWithEntity:entityDescription insertIntoManagedObjectContext:nil];
        
        
        [managedData setValue:[NSNumber numberWithInt:1] forKey:@"seq"];
        [managedData setValue:self.strURL forKey:@"url"];
        [managedData setValue:createDate forKey:@"createDate"];
        
        
        [managedObjectContext insertObject:managedData];
        
        [managedObjectContext save:&error];
        
        [managedData release];
        managedData = nil;
        managedObjectContext = nil;
        entityDescription = nil;
        
        /////////////////////////////////////////////////////////////////////////
        
        
        
        
        AkMainModel *oModel = [[AkMainModel alloc] init];
        
        oModel.delegate = self;
        
        //테스트
        //CGRect frame = CGRectMake(0.0, 35.0, 320, 328);
        
        CGRect frame = CGRectMake(0.0, 0.0, 320, 413);
        
        self.webView = [oModel mainSubWebView:self.strURL];
        
        self.webView.frame = frame;
        
        
        //[[UIApplication sharedApplication] openURL:[NSURL URLWithString:self.strURL]];
        
        [self.contentView addSubview:self.webView];
        
        
        //[oModel release];
        //oModel = nil;
    }
}



-(void)afterAction
{
    MSG(@"AK플라자", @"확인", @"QR코드가 정상적으로 인식되지 않고있습니다");
    
    //[self dismissModalViewControllerAnimated:YES];//p65458 ios8 not action -> block
    [self.reader dismissModalViewControllerAnimated:YES];//p65458 20150511 ios8 not action -> add
    
    AkBarcodeHistoryListView *subView = [[AkBarcodeHistoryListView alloc] init];
    
    //뒤로 갈때 새로고침하기 
    [self.navigationController viewWillAppear:YES];
    [self.navigationController popViewControllerAnimated:YES];
    
  
}



// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.strURL = nil;
    
    self.tabView = [[GlobalValues sharedSingleton] tabBar];
    

    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    

    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    //self.reader = [ZBarReaderViewController new];
    
    reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    
    
    //바코드 화면
//    2013.11.14 changuk 수정
//    UIView *barcodeview = [[UIView alloc]initWithFrame:CGRectMake(0.0f, 0.0f, 320.0f, 460.0f)];
//    
//    UIImageView *barcodedown = [[UIImageView alloc]initWithFrame:CGRectMake(0.0f, 0.0f, 320.0f, 480.0f)];
//    
//    [barcodedown setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"qr_scan" ofType:@"png"]]];
    UIView *barcodeview = [[UIView alloc]initWithFrame:CGRectMake(0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.width, [[UIScreen mainScreen]bounds].size.height)];
    
    UIImageView *barcodedown = [[UIImageView alloc]initWithFrame:CGRectMake(0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.width, [[UIScreen mainScreen]bounds].size.height)];
    
    [barcodedown setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:ASSET_BY_SCREEN_HEIGHT(@"qr_scan", @"qr_scan-568h@2x") ofType:@"png"]]];
    
    [barcodeview addSubview:barcodedown];
    
    UILabel *lblQrTitle = [[UILabel alloc] initWithFrame:CGRectMake(87.0f, 15.0f, 146.0f, 20.0f)];
    
    lblQrTitle.text = @"QR코드 검색";
    //lblTitle.alpha = 0.4f;
    lblQrTitle.textColor = kFontColorWhite;
    lblQrTitle.textAlignment = UITextAlignmentCenter;
    lblQrTitle.font =kFontSizeNaviTitle;
    lblQrTitle.backgroundColor = [UIColor clearColor];
    
    [barcodeview addSubview:lblQrTitle];
    
    //receipt_title
    
    /*
    UIImageView *barcodeInfo = [[UIImageView alloc]initWithFrame:CGRectMake(0.0f, 50.0f, 320.0f, 35.0f)];
    
    [barcodeInfo setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"qr_title" ofType:@"png"]]];
    
    [barcodeview addSubview:barcodeInfo];
    
    
    
     UILabel *lblDescription = [[UILabel alloc] initWithFrame:CGRectMake(0.0f, 50.0f, 320.0f, 35.0f)];
     
     lblDescription.text = @"QR코드를 선 안에 정확히 맞추시면 자동검색 됩니다";
     //lblDescription.alpha = 0.4f;
     lblDescription.textColor = kFontColorWhite;
     lblDescription.textAlignment = UITextAlignmentCenter;
     lblDescription.font = [UIFont systemFontOfSize:12];
     lblDescription.backgroundColor = [UIColor colorWithWhite:0.000 alpha:0.400];
     
     
     [barcodeview addSubview:lblDescription];
     */
    
    UIButton* btnBarcodeLeft = [UIButton buttonWithType:UIButtonTypeCustom];
    
    btnBarcodeLeft.frame = CGRectMake(5, 10, 49, 29);
    
    [btnBarcodeLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
    [btnBarcodeLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    [btnBarcodeLeft addTarget:self action:@selector(doBarcodeLeftButton:) forControlEvents:UIControlEventTouchUpInside];
    
    
    UIButton* btnBarcodeRight = [UIButton buttonWithType:UIButtonTypeCustom];
    
    btnBarcodeRight.frame = CGRectMake(259, 10, 49, 29);
    
    [btnBarcodeRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_history" ofType:@"png"]] forState:UIControlStateNormal];
    [btnBarcodeRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_history_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    
    [btnBarcodeRight addTarget:self action:@selector(clickSubBarcodeHistory:) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    [barcodeview addSubview:btnBarcodeLeft];
    [barcodeview addSubview:btnBarcodeRight];
    
    reader.cameraOverlayView = barcodeview;
    reader.showsZBarControls = NO;
    
    [lblQrTitle release];
    lblQrTitle = nil;
    //[lblDescription release];
    //lblDescription = nil;
    [barcodedown release];
    barcodedown = nil;
    [barcodeview release];
    barcodeview = nil;
    
    
    //이 주석은 카메라화면이 뜨는 곳에 다른 뷰(이미지)를 더 추가하기 위해서 작성한 코드이다.
    
    
    ZBarImageScanner *scanner = reader.scanner;
    
    //QR코드 
    [scanner setSymbology: 0
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    [scanner setSymbology: ZBAR_QRCODE
                   config: ZBAR_CFG_ENABLE
                       to: 1];
    //1차원
    //[scanner setSymbology:ZBAR_I25 config:ZBAR_CFG_ENABLE to:0];
    [self presentModalViewController:reader animated:YES];
    self.tabView.tabBarHolder.hidden = YES;
    
    self.reader = reader;//p65458 20150511 ios8 not action -> add
    
    //20초 있다가 ...
    //[self performSelector: @selector(afterAction) withObject: nil afterDelay: 20.0];
    self.barcodeTimer = [NSTimer  scheduledTimerWithTimeInterval:(20.0) target:self selector:@selector(afterAction)  userInfo:nil repeats:NO];
    
    
//  changuk 2014.10.06 iOS8에서 오류남으로 주석처리
//    [scanner release];
//    scanner = nil;
//    [reader release];
//    reader = nil;
    
    
    
    
    
}




-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info 
{
    //카메라 인식이 20초전에 끝나면 타이머 끝내버리기 
    [self.barcodeTimer invalidate];
    self.barcodeTimer = nil;
    
    
	id<NSFastEnumeration> results = [info objectForKey:ZBarReaderControllerResults];
	
	ZBarSymbol *symbol = nil;
	for(symbol in results)
		break;
	
	//barcode_number.text = symbol.data;
    
    DLog(@"symbol.data : %@", symbol.data);
    
    
    NSRange strRangeWebPlaza = [symbol.data rangeOfString:@"www.akplaza.com"];
    NSRange strRangeMobilePlaza = [symbol.data rangeOfString:@"m.akplaza.com"];
    NSRange strRangeWebMall = [symbol.data rangeOfString:@"www.akmall.com"];
    NSRange strRangeMobileMall = [symbol.data rangeOfString:@"m.akmall.com"];
    
    self.strURL = symbol.data ;
    
    //카메라 닫히기 
    //[self dismissModalViewControllerAnimated:YES];//p65458 ios8 not action -> block
    [self.reader dismissModalViewControllerAnimated:NO];//p65458 20150511 ios8 not action -> add
    
    if (strRangeWebPlaza.location == NSNotFound 
        && strRangeMobilePlaza.location == NSNotFound 
        && strRangeWebMall.location == NSNotFound 
        && strRangeMobileMall.location == NSNotFound) 
    {
        

        
        //그 외의 URL은 사파리로 보낸다 
        
        
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:symbol.data]];
        
        //20초 타이머는 끝내라 
        [self.barcodeTimer invalidate];
        
        //뒤로 갈때 새로고침하기 
        [self.navigationController viewWillAppear:YES];
        [self.navigationController popViewControllerAnimated:YES];
        
    
        
        
    }
    else
    {
        //p65458 20150511 add 내부 url시 webview로딩 부분이 없어서 추가
        //NSString *url = @"http://www.daum.net";
        
        AkBarcodeHistoryDetailView *barcodeHistoryDetail = [[AkBarcodeHistoryDetailView alloc] init] ;
        
        barcodeHistoryDetail.strURL = self.strURL;
        
        //탭바 My로 이동
        UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
        
        
        //MY 메인의 서브 뷰일때 푸쉬가 오면 루트로 옮긴 다음에 다시
        //현재 탭의 view랑 4번 탭의 뷰가 같으냐?
        
        //루트 네비 뷰에서 네비 얻어오기
        UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];
        
        
        NSLog(@"Ended %@", self.navigationController);
        [navi pushViewController:barcodeHistoryDetail animated:YES];
        //p65458 20150511 add 내부 url시 webview로딩 부분이 없어서 추가
        
        [[NSUserDefaults standardUserDefaults] setBool:YES   forKey: @"isQRCode"];
    }
    
    

    
    
	//symbol.data가 URL이다 (URL이 아니면 alert띄우기)
	
    
    //strViewType = kBarcodeType;
    
  
    
    /*
     if(picker.sourceType == UIImagePickerControllerSourceTypeCamera)
     [self performSelector: @selector(playBeep)
     withObject: nil
     afterDelay: 0.01];
     이부분은 스캔이 되었을때, 삐 소리가 하게 하는 부분이다.
     
     */
    
 	//카메라 닫히기 
    //[self dismissModalViewControllerAnimated:YES];//p65458 ios8 not action -> block
    //[self.reader dismissModalViewControllerAnimated:YES];//p65458 20150511 ios8 not action -> add
	
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
