//
//  AkReceiptBarcodeRegView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 28..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//reader

#import "AkReceiptBarcodeRegView.h"
#import "DataFormatter.h"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

// changuk 2013.10.02 add 아이폰5 대응 이미지 매크로
#define ASSET_BY_SCREEN_HEIGHT(regular, longScreen) ((    [[UIScreen mainScreen] bounds].size.height) == 480 ? regular:longScreen)

@implementation AkReceiptBarcodeRegView
@synthesize tabView;
@synthesize txtAmount, txtBarcode;
@synthesize activityView;
@synthesize barcodeTimer;

#pragma mark - View lifecycle end
-(void)dealloc
{

    [tabView release];
    [txtBarcode release];
    [txtAmount release];
    [activityView release];
    [barcodeTimer release];
    
    [super dealloc];
}

- (void)viewDidUnload
{

    self.tabView = nil;
    self.txtBarcode = nil;
    self.txtAmount = nil;
    self.activityView = nil;
    self.barcodeTimer = nil;

    
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
-(void)closeView
{
    [self.txtAmount resignFirstResponder];
    
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    self.tabView.tabBarHolder.hidden = NO;
}

-(void)doBarcodeLeftButton:(id)sender
{
    
    [self.barcodeTimer invalidate];
    
    [self dismissModalViewControllerAnimated:YES];
    
    [self closeView];
}


-(void)doLeftButton:(id)sender
{
    [self closeView];
}



-(void)afterAction
{
    MSG(@"AK플라자", @"확인", @"영수증 바코드가 정상적으로 인식되지 않고있습니다\n직접입력바랍니다");
    
    [self dismissModalViewControllerAnimated:YES];
}




-(void)clickAppleReg:(id)sender
{
    OLogin *oLogin = [OLogin loadOLoginContextFromUserDefaults];
    
    
    if (oLogin.isLogin) {
        
        NSString *strReceiptNo = self.txtBarcode.text;
        NSString *strAmount = self.txtAmount.text;
        
        
        NSRange rangReceiptNo = [strReceiptNo rangeOfString:@"9" options:0 range:NSMakeRange(0, 1)];
        
        NSDate *now = [NSDate date];
        NSCalendar *cal = [NSCalendar currentCalendar];
        
        // we're just interested in the month and year components
        NSDateComponents *nowComps = [cal components:(NSYearCalendarUnit|NSMonthCalendarUnit) 
                                            fromDate:now];
        NSInteger month = [nowComps month];
        
        
        //월이 1자리면 0붙여서 2자리로 만들기 
//        NSString *strMonth = [NSString stringWithFormat:@"%02d", month];
        
//        NSString *strReceiptMonth = [strReceiptNo substringWithRange:NSMakeRange(4, 2)];
        
//        DLog(@"strMonth : %@,  strReceiptMonth: %@",  strMonth, strReceiptMonth);
        
        
        //당월 체크 하기 
        //NSRange rangReceiptMonth = [strReceiptNo rangeOfString:strMonth options:0 range:NSMakeRange(4, 2)];
        

        
        if (strReceiptNo.length <= 0 || strReceiptNo.length > 18) {
            MSG(nil, @"확인", @"유효한 영수증 바코드 번호가 아닙니다!");
        } else if (rangReceiptNo.location == NSNotFound) {
            MSG(nil, @"확인", @"유효한 영수증 바코드 번호가 아닙니다!");
//        } else if (![strMonth isEqualToString:strReceiptMonth]) {
//            MSG(nil, @"확인", @"당월 영수증만 적립 가능합니다!");
        }
        else if (strAmount.length  <= 0 || strAmount == nil || strAmount == @"0")
        {
            MSG(nil, @"확인", @"결제금액을 입력하세요");
        }
        else
        {
            //위에 4가지 체크 다 되면 애플적립 시작~~~ 
//            MSG_DELEGATE_BTN2(nil, @"취소", @"적립", @"영수증 등록 후 애플적립하시겠습니까?", self);
            // 경고창 없이 바로 등록
            AkReceiptRegModel *oModel = [[AkReceiptRegModel alloc] init];
            
            oModel.delegate = self;
            
            NSString *strReceiptNo = self.txtBarcode.text;
            NSString *strAmount = self.txtAmount.text;
            
            [oModel performSelectorOnMainThread:@selector(receiptReg:) withObject:[NSString stringWithFormat: @"%@,%@", strReceiptNo, strAmount]  waitUntilDone:YES];
            
            [self closeView]; 
        }
    }
    else
    {
        LoginViewController *loginView = [[LoginViewController alloc] init];
        
        loginView.isLeftBtn = YES;
        
        [self.navigationController pushViewController:loginView animated:YES];
        
        loginView = nil;
    }
    
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0) {
        
    }
    else
    {


        
        //실패
        //MSG_NORMAL(@"애플 적립 실패", @"확인");
    }
    
}



- (void)onBtnBackground:(id)sender {
    NSArray *subs = self.contentView.subviews;
    
    UIView* curView;
    UIView* cView;
    
    for (curView in subs) {
        
        for (cView in curView.subviews) {
            
            if ([cView conformsToProtocol:@protocol(UITextInputTraits)]) 
            {
                [cView resignFirstResponder];
            }
        }
    }
}


-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSString *strText = textField.text;
    int nTag = textField.tag;
    
    NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];

    NSNumber *candidateNumber = [numberFormatter numberFromString:textField.text];


    
    
    if (nTag == 999) 
    {
        if (string.length == 0 || strText.length < 18) 
        {
            //계속 입력할수 있다.
            
            return  YES;
        }
        else
        {
            MSG_NORMAL(@"더이상 입력할수 없습니다", @"확인");
            return NO;
        }
    }

    
    if (nTag == 888) 
    {
        if (string.length == 0 || strText.length < 13) 
        {
            //계속 입력할수 있다.
            
            //NSString *newText = [textField.text stringByReplacingCharactersInRange:range withString:string];
            //[textField setText:[DataFormatter moneyFormat:newText]];
            
            return  YES;
        }
        else
        {
            MSG_NORMAL(@"더이상 입력할수 없습니다", @"확인");
            return NO;
        }

    }
    
    
    if ([string length] > 0) {
        
        //백스페이스는 체크하지 않아야 한다. 
        
        //nil이면 숫자가 아니므로 NO 리턴해서 입력취소
        if(candidateNumber == nil) {
            //MSG_NORMAL(@"숫자만 입력가능합니다", @"확인");
            return NO;
        }
        
        //원래 문자열과 숫자로 변환한 후의 값이 문자열 비교시 다르면
        //숫자가 아닌 부분이 섞여있다는 의미임
        if ([[candidateNumber stringValue] compare:strText] != NSOrderedSame) {
            //MSG_NORMAL(@"숫자만 입력가능합니다", @"확인");
            return NO; 
        }
        
    }
    
    
    
    return NO;
}


#pragma mark - View lifecycle start


// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
}

-(void)viewWillAppear:(BOOL)animated
{
    
    [super viewWillAppear:animated];
    
    self.tabView.tabBarHolder.hidden = NO;
    
    [self.txtAmount becomeFirstResponder];
}






// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];

    
    
    self.tabView = [[GlobalValues sharedSingleton] tabBar];
    
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    
//	reader = [ZBarReaderViewController new];
	reader.readerDelegate = self;
	
    
    //바코드 화면
    UIView *barcodeview = [[UIView alloc]initWithFrame:CGRectMake(0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.width, [[UIScreen mainScreen]bounds].size.height)];
    
    UIImageView *barcodedown = [[UIImageView alloc]initWithFrame:CGRectMake(0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.width, [[UIScreen mainScreen]bounds].size.height)];
    
    [barcodedown setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:ASSET_BY_SCREEN_HEIGHT(@"barcodeBg", @"barcodeBg-568h@2x") ofType:@"png"]]];
    
    [barcodeview addSubview:barcodedown];
    
    UILabel *lblTitle = [[UILabel alloc] initWithFrame:CGRectMake(87.0f, 15.0f, 146.0f, 20.0f)];
    
    lblTitle.text = @"영수증 등록";
    //lblTitle.alpha = 0.4f;
    lblTitle.textColor = kFontColorWhite;
    lblTitle.textAlignment = UITextAlignmentCenter;
    lblTitle.font = kFontSizeNaviTitle;
    lblTitle.backgroundColor = [UIColor clearColor];
    
    [barcodeview addSubview:lblTitle];
    
    
    //receipt_title
    /*
    UIImageView *barcodeInfo1 = [[UIImageView alloc]initWithFrame:CGRectMake(0.0f, 50.0f, 320.0f, 35.0f)];
    
    [barcodeInfo1 setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"receipt_title" ofType:@"png"]]];
    
    [barcodeview addSubview:barcodeInfo1];

    
    
    UILabel *lblDescription = [[UILabel alloc] initWithFrame:CGRectMake(0.0f, 50.0f, 320.0f, 35.0f)];
    
    lblDescription.text = @"AK PLAZA 영수증 바코드를 선 안에 정확히 맞추시면 자동검색 됩니다";
    //lblDescription.alpha = 0.4f;
    lblDescription.textColor = kFontColorWhite;
    lblDescription.textAlignment = UITextAlignmentCenter;
    lblDescription.font = [UIFont systemFontOfSize:10];
    lblDescription.backgroundColor = [UIColor colorWithWhite:0.000 alpha:0.400];
    
    
    [barcodeview addSubview:lblDescription];
    
     */
    
    
    UIButton* btnBarcodeLeft = [UIButton buttonWithType:UIButtonTypeCustom];
    
    btnBarcodeLeft.frame = CGRectMake(5, 10, 49, 29);
    
    [btnBarcodeLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
    [btnBarcodeLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    [btnBarcodeLeft addTarget:self action:@selector(doBarcodeLeftButton:) forControlEvents:UIControlEventTouchUpInside];
    
    [barcodeview addSubview:btnBarcodeLeft];

    /*
    UIImageView *barcodeInfo2 = [[UIImageView alloc]initWithFrame:CGRectMake(40.0f, 315.0f, 240.0f, 45.0f)];
    
    [barcodeInfo2 setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"receipt_apple" ofType:@"png"]]];
    
    [barcodeview addSubview:barcodeInfo2];

    */
    
    
    reader.cameraOverlayView = barcodeview;
    reader.showsZBarControls = NO;
    
    [lblTitle release];
    lblTitle = nil;
    //[lblDescription release];
    //lblDescription = nil;
    [barcodedown release];
    barcodedown = nil;
    [barcodeview release];
    barcodeview = nil;
    
    
    //이 주석은 카메라화면이 뜨는 곳에 다른 뷰(이미지)를 더 추가하기 위해서 작성한 코드이다.
 	
	
	ZBarImageScanner *scanner = reader.scanner;
	
	//1차원 바코드
// changuk 2013.10.02 I25, CODE128 을 지원하도록 수정
//	[scanner setSymbology:ZBAR_I25 config:ZBAR_CFG_ENABLE to:0];
    [scanner setSymbology:0 config:ZBAR_CFG_ENABLE to:0];
    [scanner setSymbology:ZBAR_I25 config:ZBAR_CFG_ENABLE to:1];
    [scanner setSymbology:ZBAR_CODE128 config:ZBAR_CFG_ENABLE to:1];
    
	[self presentModalViewController:reader animated:YES];
    self.tabView.tabBarHolder.hidden = YES;
	
    
    //20초 있다가 ...
    //[self performSelector: @selector(afterAction) withObject: nil afterDelay: 20.0];
    self.barcodeTimer = [NSTimer  scheduledTimerWithTimeInterval:(20.0) target:self selector:@selector(afterAction)  userInfo:nil repeats:NO];
    
//  changuk 2014.10.06 iOS8에서 오류남으로 주석처리
//	[scanner release];
//    scanner = nil;
//	[reader release];
//    reader = nil;
    
    
    //키보드 나타날때 아무곳이나 터치하면 사라지게 하기 
    UIButton *btnTextFieldHidden = [UIButton buttonWithType:UIButtonTypeCustom];
    btnTextFieldHidden.frame = CGRectMake(0, 0, 320, 400);
    [btnTextFieldHidden addTarget:self action:@selector(onBtnBackground:) forControlEvents:UIControlEventTouchUpInside];
    btnTextFieldHidden.userInteractionEnabled   = YES;
    
    [self.contentView addSubview:btnTextFieldHidden ];
    
    
    UIView *titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 35)];
    titleView.backgroundColor = kTitleBgColorBrown;
    
    CALayer *bottom = [CALayer layer];
    bottom.backgroundColor = kTitleBorderColor.CGColor;
    bottom.frame = CGRectMake(0, titleView.frame.size.height-kTitleBorderWidth, titleView.frame.size.width, kTitleBorderWidth);
    [titleView.layer addSublayer:bottom];
    
    
    UIImageView *titleBuletImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"title_bulet" ofType:@"png"]]];
    
    titleBuletImg.frame = CGRectMake(10, 9, 8, 17);
    
    [titleView addSubview:titleBuletImg];
    
    UILabel *lblBarcodeTitle = [[UILabel alloc] initWithFrame:CGRectMake(19, 9, 291, 17)];
    lblBarcodeTitle.backgroundColor = [UIColor clearColor];
    lblBarcodeTitle.text = @"영수증 바코드 직접 입력";
    lblBarcodeTitle.textColor = kFontTitleColor;
    lblBarcodeTitle.textAlignment = UITextAlignmentLeft;
    lblBarcodeTitle.font = kFontSizeTitle;
    
    [titleView addSubview:lblBarcodeTitle];
    
    [self.contentView addSubview:titleView];
    
    [lblBarcodeTitle release];
    lblBarcodeTitle = nil;
    [titleView release];
    titleView = nil;
    [titleView release];
    titleView = nil;
    

    
    
    UIImageView *barcodeInputBG = [[UIImageView alloc]initWithFrame:CGRectMake(10,55,300,89)];
    
    barcodeInputBG.userInteractionEnabled = YES;
    
    [barcodeInputBG setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"receipt_num_input" ofType:@"png"]]];
    
    [self.contentView addSubview:barcodeInputBG];
    
//    UILabel *lblBarcode = [[UILabel alloc] initWithFrame:CGRectMake(7,8, 98, 19)];
//    lblBarcode.backgroundColor = [UIColor clearColor];
//    lblBarcode.text = @"영수증 바코드 번호";
//    lblBarcode.textColor = kFontBrownColor;
//    lblBarcode.textAlignment = UITextAlignmentLeft;
//    lblBarcode.font = kFontSizeDefault;
//
//    
//    [barcodeInputBG addSubview:lblBarcode];

    
    self.txtBarcode = [[UITextField alloc] initWithFrame:CGRectMake(10, 0, 280, 44)];
    self.txtBarcode.tag = 999;
    self.txtBarcode.font = kFontSizeLarge;
    self.txtBarcode.placeholder = @"영수증 바코드 번호";
    self.txtBarcode.textColor = kFontBrownColor;
    self.txtBarcode.backgroundColor = [UIColor clearColor];
    self.txtBarcode.keyboardType = UIKeyboardTypeNumberPad;  
    
    [self.txtBarcode setDelegate:self];
    [barcodeInputBG addSubview:txtBarcode];
    
    
//    UILabel *lblAmount = [[UILabel alloc] initWithFrame:CGRectMake(7, 43, 98,19)];
//    lblAmount.text = @"총 결제 금액";
//    lblAmount.font = kFontSizeDefault;
//    lblAmount.textColor = kFontBrownColor;
//    lblAmount.backgroundColor = [UIColor clearColor];
//    [barcodeInputBG addSubview:lblAmount];
//    [lblAmount release];
//    lblAmount = nil;
    
    self.txtAmount = [[UITextField alloc] initWithFrame:CGRectMake(10, 45, 280, 44)];
    self.txtAmount.backgroundColor = [UIColor clearColor];
    self.txtAmount.font = kFontSizeLarge;
    self.txtAmount.placeholder = @"총 결제 금액";
    self.txtAmount.textColor = kFontBrownColor;
    self.txtAmount.keyboardType = UIKeyboardTypeNumberPad;  
    self.txtAmount.tag = 888;
    
    [self.txtAmount setDelegate:self];
    [barcodeInputBG addSubview:txtAmount];
    
    
    
    UIButton *btnAppleReg = [UIButton buttonWithType:UIButtonTypeCustom];
    
    //[btnAppleReg setTitle:@"애플 적립하기" forState:UIControlStateNormal];
    
    btnAppleReg.frame = CGRectMake(105,154,110,35);
    
    [btnAppleReg setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"new_receipt_regist" ofType:@"png"]] forState:UIControlStateNormal];
    [btnAppleReg setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"new_receipt_regist" ofType:@"png"]] forState:UIControlStateHighlighted];

    
    
    [btnAppleReg addTarget:self action:@selector(clickAppleReg:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.contentView addSubview:btnAppleReg];

    btnAppleReg = nil;
    
    
    //바코드 등록 설명 ///
    UIView *InfoBgView = [[UIView alloc] initWithFrame:CGRectMake(0, 214, 320, 149)];
    InfoBgView.backgroundColor = [UIColor clearColor]; //kLoginInfoBgColor ;//
    
    [self.contentView addSubview:InfoBgView];
    
    /*
    UILabel *lblInfo = [[UILabel alloc] initWithFrame:CGRectMake(15, 20, 290, 15)];
    lblInfo.backgroundColor = [UIColor clearColor];
    lblInfo.text = @"영수증 바코드란?";
    lblInfo.textColor = kFontBrownColor;
    lblInfo.textAlignment = UITextAlignmentLeft;
    lblInfo.font = kFontSizeLarge;
    
    [InfoBgView addSubview:lblInfo];
    */
    //
    UIImageView *textBuletImg1 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
    
    textBuletImg1.frame = CGRectMake(10, 0, 6.5, 16);
    
    [InfoBgView addSubview:textBuletImg1];
    
    UILabel *lblInfo1 = [[UILabel alloc] initWithFrame:CGRectMake(16.5, 0,  300, 32)];
    lblInfo1.backgroundColor = [UIColor clearColor];
    lblInfo1.text = @"구매하신 물품 영수증의 하단에 [18자리 바코드]를\n 확인하세요";
    lblInfo1.numberOfLines = 2;
    lblInfo1.textColor = kFontDefaultFontColor;
    lblInfo1.textAlignment = UITextAlignmentLeft;
    lblInfo1.font =  kFontSizeDefault;
    
    [InfoBgView addSubview:lblInfo1];   
    
    //
    UIImageView *textBuletImg2 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
    
    textBuletImg2.frame = CGRectMake(10, 35, 6.5, 16);
    
    [InfoBgView addSubview:textBuletImg2];
    
    UILabel *lblInfo2 = [[UILabel alloc] initWithFrame:CGRectMake(16.5, 35, 300, 32)];
    lblInfo2.backgroundColor = [UIColor clearColor];
    lblInfo2.text = @"영수증 바코드 적립기간을 구매하신 영수증 발생일로부터 30일간 적립 유효합니다";
    lblInfo2.textColor = kFontDefaultFontColor;
    lblInfo2.numberOfLines = 2;
    lblInfo2.textAlignment = UITextAlignmentLeft;
    lblInfo2.font = kFontSizeDefault;
    
    [InfoBgView addSubview:lblInfo2];  
    
    //
    
    // 애플적립 텍스트 문구 변경
    /*
    UIImageView *textBuletImg3 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
    
    textBuletImg3.frame = CGRectMake(10, 70, 6.5, 16);
    
    [InfoBgView addSubview:textBuletImg3];
    
    UILabel *lblInfo3 = [[UILabel alloc] initWithFrame:CGRectMake(16.5, 70, 300, 32)];
    lblInfo3.backgroundColor = [UIColor clearColor];
    lblInfo3.text = @"애플 유효기간은 2개월이 지나면 애플은 자동 소멸됩니다";
    lblInfo3.textColor = kFontBrownColor;
    lblInfo3.numberOfLines = 2;
    lblInfo3.textAlignment = UITextAlignmentLeft;
    lblInfo3.font = kFontSizeDefault;
    
    [InfoBgView addSubview:lblInfo3];  
    
    //
    UIImageView *textBuletImg4 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
    
    textBuletImg4.frame = CGRectMake(10, 105, 6.5, 16);
    
    [InfoBgView addSubview:textBuletImg4];
    UILabel *lblInfo4 = [[UILabel alloc] initWithFrame:CGRectMake(16.5, 105, 300, 32)];
    lblInfo4.backgroundColor = [UIColor clearColor];
    lblInfo4.text = @"애플적립은 [ALL > MY메뉴 > 애플적립내역]에서 확인 가능합니다";
    lblInfo4.textColor = kFontBrownColor;
    lblInfo4.numberOfLines = 2;
    lblInfo4.textAlignment = UITextAlignmentLeft;
    lblInfo4.font = kFontSizeDefault;
    
    [InfoBgView addSubview:lblInfo4];  
    */
    
    [InfoBgView release];
    InfoBgView = nil;
    
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
	//symbol.data가 바코드 번호이다.
	//self.strURL = symbol.data;
	
    
    /*
     if(picker.sourceType == UIImagePickerControllerSourceTypeCamera)
     [self performSelector: @selector(playBeep)
     withObject: nil
     afterDelay: 0.01];
     이부분은 스캔이 되었을때, 삐 소리가 하게 하는 부분이다.
     
     */
    
 	[self dismissModalViewControllerAnimated:YES];
    
    self.tabView.tabBarHolder.hidden = NO;
    
    //10초내로 인식못하면 팝업 띄어서 직접입력하게 화면이동 시키기 

    self.txtBarcode.text = symbol.data;
    
	
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
