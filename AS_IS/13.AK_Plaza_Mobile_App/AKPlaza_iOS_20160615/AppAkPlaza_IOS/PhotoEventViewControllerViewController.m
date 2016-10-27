//
//  PhotoEventViewControllerViewController.m
//  AppAkPlaza_IOS
//
//  Created by 김 두민 on 12. 5. 22..
//  Copyright (c) 2012년 puuu80@uniwis.com. All rights reserved.
//

#import "PhotoEventViewControllerViewController.h"
#import "GlobalValues.h"
#import "PhotoEventProc.h"
#import "AkMainView.h"
#import "ImageUtil.h"

@implementation PhotoEventViewControllerViewController

@synthesize tf_Title;
@synthesize tv_Contents;
@synthesize iv_Attach;
@synthesize btn_Attach_bgView;
@synthesize vw_BackWorkingView;
@synthesize vw_BottomContainer;
@synthesize sclv_ParentContainer;

bool flag;

#define kPhotoSizeWidth 130
#define kPhotoSizeHeight 190
#define kTvContentWidth 200

-(void)dealloc
{
    tf_Title = nil;
    [tf_Title release];
    tv_Contents = nil;
    [tv_Contents release];
    iv_Attach = nil;
    [iv_Attach release];
    vw_BackWorkingView = nil;
    [vw_BackWorkingView release];
    vw_BottomContainer = nil;
    [vw_BottomContainer release];
    btn_Attach_bgView = nil;
    [btn_Attach_bgView release];
    sclv_ParentContainer = nil;
    [sclv_ParentContainer release];
    imgPicker = nil;
    [imgPicker release];
    
    [_header release];
    [_titlebox release];
    [super dealloc];
}

#pragma mark - scrollView Delegate
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
//    NSLog(@"Ended");
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event{
//    NSLog(@"Moved");
}
- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
//    NSLog(@"Cancelled");
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [tv_Contents resignFirstResponder];
    [tf_Title resignFirstResponder];
}
-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    [tv_Contents resignFirstResponder];
    [tf_Title resignFirstResponder];
}


#pragma mark - TextField Delegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
	return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField { 
}

-(void)textFieldDidEndEditing:(UITextField *)textField {
}
-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    return YES;
}

#pragma mark - TextView Delegate
-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{   
    NSString *fixedText = [NSString stringWithFormat:@"%@%@",tv_Contents.text,text];
    float fontHeight = [self calcLabelHeightWithText:@"dummyText" TargetWidth:kTvContentWidth TargetFont:tv_Contents.font];
    
    [UIView beginAnimations:@"tv_content" context:nil];
    [UIView setAnimationDuration:0.2f];
    if(currentContentHeight < [self calcLabelHeightWithText:fixedText TargetWidth:kTvContentWidth TargetFont:tv_Contents.font]){
        [tv_Contents setFrame:(CGRectMake(tv_Contents.frame.origin.x, tv_Contents.frame.origin.y, tv_Contents.frame.size.width, tv_Contents.frame.size.height+fontHeight))];
        [vw_BottomContainer setFrame:CGRectMake(vw_BottomContainer.frame.origin.x, vw_BottomContainer.frame.origin.y+fontHeight, vw_BottomContainer.frame.size.width, vw_BottomContainer.frame.size.height)];
        [sclv_ParentContainer setContentOffset:CGPointMake(0, sclv_ParentContainer.contentOffset.y+fontHeight) animated:YES];
        currentContentHeight = [self calcLabelHeightWithText:fixedText TargetWidth:kTvContentWidth TargetFont:tv_Contents.font];
    }else if(currentContentHeight > [self calcLabelHeightWithText:fixedText TargetWidth:kTvContentWidth TargetFont:tv_Contents.font]){
        [tv_Contents setFrame:(CGRectMake(tv_Contents.frame.origin.x, tv_Contents.frame.origin.y, tv_Contents.frame.size.width, tv_Contents.frame.size.height-fontHeight))];
        [vw_BottomContainer setFrame:CGRectMake(vw_BottomContainer.frame.origin.x, vw_BottomContainer.frame.origin.y-fontHeight, vw_BottomContainer.frame.size.width, vw_BottomContainer.frame.size.height)];
        if(sclv_ParentContainer.contentOffset.y-fontHeight < 0){
            [sclv_ParentContainer setContentOffset:CGPointZero animated:YES];
        }else{
            [sclv_ParentContainer setContentOffset:CGPointMake(0, sclv_ParentContainer.contentOffset.y-fontHeight) animated:YES];
        }
        currentContentHeight = [self calcLabelHeightWithText:fixedText TargetWidth:kTvContentWidth TargetFont:tv_Contents.font];    
    }
    [UIView commitAnimations];
    
    return YES;
}

-(void)textViewDidBeginEditing:(UITextView *)textView
{
    [self calcScrViewContentSize];
    [sclv_ParentContainer setContentOffset:CGPointMake(0, sclv_ParentContainer.contentOffset.y+100) animated:YES];
}

-(void)textViewDidEndEditing:(UITextView *)textView
{
    [self calcScrViewContentSize];
    if(sclv_ParentContainer.contentOffset.y-100 < 0){
        [sclv_ParentContainer setContentOffset:CGPointZero animated:YES];
    }else{
        [sclv_ParentContainer setContentOffset:CGPointMake(0, sclv_ParentContainer.contentOffset.y-100) animated:YES];
    }
}

-(void)dismissKeyboard { 
    [tf_Title becomeFirstResponder];
    [tf_Title resignFirstResponder];
    [tv_Contents becomeFirstResponder];
    [tv_Contents resignFirstResponder];
}



#pragma mark - IBACTION IMPLEMENTS


-(IBAction)actionAddAttach:(id)sender
{
    UIActionSheet *menu = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"사진촬영", @"저장된 이미지 선택", nil];
    [menu showInView:self.view];
}

-(IBAction)actionSubmit:(id)sender
{   
    title = [tf_Title.text copy];
    contents = [tv_Contents.text copy];

    [self eventCatcherAdd:nil];
    [NSThread detachNewThreadSelector:@selector(uploadThread) toTarget:self withObject:nil];
}

-(IBAction)actionCancel:(id)sender
{
    AkMainView *sub = [[self.navigationController viewControllers] objectAtIndex:[self.navigationController.viewControllers count]-2];
    
    sub.isPhotoBack = YES;
    [[self.navigationController popViewControllerAnimated:YES]release];
}

-(IBAction)actionFirstResponderContentTextView:(id)sender
{
    [tv_Contents becomeFirstResponder];
}

#pragma mark - ActionSheet


//UIActionSheet 안의 버튼이 클릭 되었을 때 CallBack
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    imgPicker.delegate = self;
    switch (buttonIndex) {
        case 0:
            if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]){
                [imgPicker setSourceType:UIImagePickerControllerSourceTypeCamera];
            }else{
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"- Camera -"
                                                                message:@"카메라를 지원하지않습니다.\n갤러리로 이동합니다."
                                                               delegate:nil
                                                      cancelButtonTitle:@"확인"
                                                      otherButtonTitles:nil];
                [alert show];
                [alert release];
                [imgPicker setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
            }
            [self presentModalViewController:imgPicker animated:YES];
            [[GlobalValues sharedSingleton] tabBar].tabBarHolder.hidden = YES;
            break;
        case 1:
            [imgPicker setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
            [self presentModalViewController:imgPicker animated:YES];
            [[GlobalValues sharedSingleton] tabBar].tabBarHolder.hidden = YES;
            break;
    }
    [actionSheet release];
}


-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    if ([[GlobalValues sharedSingleton] majorIOSVersion] <= 4){
        //is work on ios 4.x under
        [self dismissModalViewControllerAnimated:YES];
    }else{
        //ios 5.x
        [self dismissViewControllerAnimated:YES completion:nil];
    }
    [[GlobalValues sharedSingleton] tabBar].tabBarHolder.hidden = NO;
    
    capturedImg = [ImageUtil imageWithImage:[info objectForKey:UIImagePickerControllerOriginalImage] scaledToSize:CGSizeMake(685, 1024)];
    iv_Attach.image = capturedImg;
    
    // 사진 등록시 하단 컨테이너 y값 이동 및 이미지 영역 크기 조절(130,0 -> 130,kPhotoSizeHeight)
    if(iv_Attach.frame.size.height == 0){
        [iv_Attach setFrame:CGRectMake(iv_Attach.frame.origin.x, iv_Attach.frame.origin.y, kPhotoSizeWidth, kPhotoSizeHeight)];
        [btn_Attach_bgView setFrame:CGRectMake(btn_Attach_bgView.frame.origin.x, btn_Attach_bgView.frame.origin.y, kTvContentWidth+5, kPhotoSizeHeight)];
        [vw_BottomContainer setFrame:CGRectMake(vw_BottomContainer.frame.origin.x, vw_BottomContainer.frame.origin.y+kPhotoSizeHeight, vw_BottomContainer.frame.size.width, vw_BottomContainer.frame.size.height)];
        [tv_Contents setFrame:CGRectMake(tv_Contents.frame.origin.x, tv_Contents.frame.origin.y+kPhotoSizeHeight, tv_Contents.frame.size.width, tv_Contents.frame.size.height)];
        
        // 스크롤뷰 ContentSize조절
        [self calcScrViewContentSize];
    }
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    if ([[GlobalValues sharedSingleton] majorIOSVersion] <= 4){
        //is work on ios 4.x under
        [self dismissModalViewControllerAnimated:YES];
    }else{
        //ios 5.x
        [self dismissViewControllerAnimated:YES completion:nil];
    }
    [[GlobalValues sharedSingleton] tabBar].tabBarHolder.hidden = NO;
}


#pragma mark - Photo Event Thread
-(void) uploadThread
{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    [self uploadPhoto];
    [self performSelectorOnMainThread:@selector(uploadedPhoto) withObject:self waitUntilDone:NO];
    [pool release];
}

-(void) uploadPhoto
{
    NSArray *keys;
    NSArray *vals;
    if(isJoin){
        keys = [[NSArray alloc] initWithObjects:@"act",@"type",@"event_index",@"event_token",@"title",@"content",@"name",@"phone",nil];
        vals = [[NSArray alloc] initWithObjects:act,@"json",event_index,event_token,title,contents,name,phone,nil];
    }else{
        keys = [[NSArray alloc] initWithObjects:@"act",@"type",@"event_index",@"entry_indexno",@"title",@"content",@"name",@"phone",nil];
        vals = [[NSArray alloc] initWithObjects:act,@"json",event_index,entry_indexno,title,contents,name,phone,nil];
    }
    
    PhotoEventProc *adapter = [[PhotoEventProc alloc]init];
    
    NSString *uploadResult = [adapter submitPhotoEvent:capturedImg KeyArr:keys VarArr:vals];
    
    dispatch_async(dispatch_get_main_queue(), ^{ //20151110 Minseok 얼럿이 보여지기전에 릴리즈되는문제로인해 병렬처리
        flag = (bool)[[uploadResult componentsSeparatedByString:@"&splt;"] objectAtIndex:2];
        
        [keys release];
        [vals release];
        [adapter release];
    
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                        message:[[uploadResult componentsSeparatedByString:@"&splt;"] objectAtIndex:1]
                                                       delegate:self
                                              cancelButtonTitle:@"확인"
                                              otherButtonTitles:nil];
        if([[[uploadResult componentsSeparatedByString:@"&splt;"] objectAtIndex:0] isEqualToString:@"fail"]){
            [alert setTag:1];
        }else{
            [alert setTag:0];
        }
        [alert show];
        [alert release];
    });
    
}

-(void) uploadedPhoto
{
    [self eventCatcherRmv];
}

#pragma mark - Photo Event getEntry Thread
-(void) getEntryThread
{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    [self getEntryData];
    [self performSelectorOnMainThread:@selector(gotEntryData) withObject:self waitUntilDone:NO];
    [pool release];
}

-(void) getEntryData
{
    PhotoEventProc *adapter = [[PhotoEventProc alloc]init];
    NSDictionary *totalArr = [adapter getEntryDetailByAct:@"getEntryDetail" event_index:event_index entry_indexno:entry_indexno];
    [adapter release];
    
    if([[totalArr objectForKey:@"result"] isEqualToString:@"success"]){
        NSDictionary *entryArr = [totalArr objectForKey:@"entry"];
        //// get Text Values
        contents = [[entryArr objectForKey:@"CONTENT"] copy];
        title = [[entryArr objectForKey:@"TITLE"] copy];
        userID = [[entryArr objectForKey:@"USERID"] copy];
        //// get Image
        NSString *imgUrl = [NSString stringWithFormat:@"%@%@",kRootURL, [entryArr objectForKey:@"CONTENT_IMAGE_URI"]];
        NSData *imgData = [NSData dataWithContentsOfURL:[NSURL URLWithString:imgUrl]];
        if(imgData) {
            capturedImg =[[UIImage alloc] initWithData:imgData];
        }
    }else {
        dispatch_async(dispatch_get_main_queue(), ^{ //20151110 Minseok 얼럿이 보여지기전에 릴리즈되는문제로인해 병렬처리
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                        message:[totalArr objectForKey:@"message"]
                                                       delegate:self
                                              cancelButtonTitle:@"확인"
                                              otherButtonTitles:nil];
        [alert setTag:0];
        [alert show];
        [alert release];
        });
    }
}

-(void) gotEntryData
{
    //// set UI Values
    [tf_Title setText:title];
    [tv_Contents setText:contents];
    [iv_Attach setImage:capturedImg];

    if(iv_Attach.frame.size.height == 0){
        [iv_Attach setFrame:CGRectMake(iv_Attach.frame.origin.x, iv_Attach.frame.origin.y, kPhotoSizeWidth, kPhotoSizeHeight)];
        [btn_Attach_bgView setFrame:CGRectMake(btn_Attach_bgView.frame.origin.x, btn_Attach_bgView.frame.origin.y, kTvContentWidth+5, kPhotoSizeHeight)];
        [vw_BottomContainer setFrame:CGRectMake(vw_BottomContainer.frame.origin.x, vw_BottomContainer.frame.origin.y+kPhotoSizeHeight, vw_BottomContainer.frame.size.width, vw_BottomContainer.frame.size.height)];
        [tv_Contents setFrame:CGRectMake(tv_Contents.frame.origin.x, tv_Contents.frame.origin.y+kPhotoSizeHeight, tv_Contents.frame.size.width, tv_Contents.frame.size.height)];
    }
    float contentHeight = [self calcLabelHeightWithText:contents TargetWidth:kTvContentWidth TargetFont:tv_Contents.font] + 15;
    
    [tv_Contents setFrame:(CGRectMake(tv_Contents.frame.origin.x, tv_Contents.frame.origin.y, tv_Contents.frame.size.width, contentHeight))];
    [vw_BottomContainer setFrame:CGRectMake(vw_BottomContainer.frame.origin.x, vw_BottomContainer.frame.origin.y+contentHeight-47, vw_BottomContainer.frame.size.width, vw_BottomContainer.frame.size.height)];
    currentContentHeight = contentHeight-15;
    
    // 스크롤뷰 ContentSize조절
    [self calcScrViewContentSize];
    
    [self eventCatcherRmv];
}


#pragma mark - UIAlertView Delegate
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    switch ([alertView tag]) {
        case 0:
            [self actionCancel:nil];
            break;
            
        case 1:
            break;
    }
    
}

#pragma mark - custom

-(void) setParamTokensUserID:(NSString*)pUserId act:(NSString*)pAct type:(NSString*)pType event_index:(NSString*)pEvent_index event_token:(NSString*)pEvent_token name:(NSString *) pname phone:(NSString *) pphone;
{
    userID = pUserId;
    act = pAct;
    isJoin = YES;
    type = pType;
    event_index = pEvent_index;
    event_token = pEvent_token;
    name = pname;
    phone = pphone;
    if(name == NULL) {
        name = @"";
    }
    if(phone == NULL) {
        phone = @"";
    }
}


-(void) setParamTokensForUpdateUserID:(NSString*)pUserId act:(NSString*)pAct type:(NSString*)pType event_index:(NSString*)pEvent_index entry_indexno:(NSString*)pEntry_indexno name:(NSString *) pname phone:(NSString *) pphone
{
    userID = pUserId;
    act = pAct;
    isJoin = NO;
    type = pType;
    event_index = pEvent_index;
    entry_indexno = pEntry_indexno;
    name = pname;
    phone = pphone;
    if(name == NULL) {
        name = @"";
    }
    if(phone == NULL) {
        phone = @"";
    }
}

-(void) eventCatcherAdd:(NSString*)message
{
    [self dismissKeyboard];
    [vw_BackWorkingView setFrame:CGRectMake(0, 0, self.view.bounds.size.width, self.view.bounds.size.height)];
    [vw_BackWorkingView setAlpha:0.0f];
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration: 0.5f];
    [vw_BackWorkingView setAlpha:1.0f];
    [UIView commitAnimations];
    [vw_BackWorkingView removeFromSuperview];
    [self.view addSubview:vw_BackWorkingView];
}

-(void) eventCatcherRmv
{
    [vw_BackWorkingView setAlpha:1.0f];
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration: 0.5f];
    [vw_BackWorkingView setAlpha:0.0f];
    [UIView commitAnimations];
    [vw_BackWorkingView removeFromSuperview];
}

-(void) calcScrViewContentSize
{
    CGFloat scrollViewHeight = 0.0f;
    for (UIView* view in self.sclv_ParentContainer.subviews)
    {
        if (!view.hidden)
        {
            CGFloat y = view.frame.origin.y;
            CGFloat h = view.frame.size.height;
            if (y + h > scrollViewHeight)
            {
                scrollViewHeight = h + y;
            }
        }
    }
    [self.sclv_ParentContainer setContentSize:(CGSizeMake(self.sclv_ParentContainer.frame.size.width, scrollViewHeight))];
}

-(float) calcLabelHeightWithText:(NSString*)targetText TargetWidth:(NSInteger)width TargetFont:(UIFont*)font
{
    return [targetText sizeWithFont:font constrainedToSize:CGSizeMake(width, 10000) lineBreakMode:UILineBreakModeWordWrap].height;
}

#pragma mark - life

- (void)viewDidLoad
{
    [super viewDidLoad];
    if(imgPicker == nil){
        imgPicker = [[UIImagePickerController alloc] init];
        [imgPicker setAllowsEditing:NO];
    }
    [tv_Contents setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_textarea_02"]]];
    
    if(!isJoin){
        [self eventCatcherAdd:nil];
        [NSThread detachNewThreadSelector:@selector(getEntryThread) toTarget:self withObject:nil];
    }
    
    CALayer *bottom = [CALayer layer];
    bottom.backgroundColor = kTitleBorderColor.CGColor;
    bottom.frame = CGRectMake(0, self.titlebox.frame.size.height-kTitleBorderWidth, self.titlebox.frame.size.width, kTitleBorderWidth);
//    [self.titlebox.layer addSublayer:bottom];
    
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=appHeader",kLibURL]];
    self.header.delegate = self;
    [self.header loadRequest:[NSURLRequest requestWithURL:url]];
}

-(void)viewWillAppear:(BOOL)animated
{
    flag = false;
}

- (void)viewWillDisappear:(BOOL)animated
{
    if(flag){
        [[[GlobalValues sharedSingleton] tabBar].webView reload];
    }
}

- (void)viewDidUnload
{
    [self setHeader:nil];
    [self setTitlebox:nil];
    [super viewDidUnload];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSString *strURL = [[request URL]absoluteString];
    if([strURL rangeOfString:@"appHeader"].length > 0) {
        return true;
    }
    UWTabBar *tabBar = [[GlobalValues sharedSingleton] tabBar];
    tabBar.currentIndex = 0;
    [tabBar.webView loadRequest:request];
    [self.navigationController popToRootViewControllerAnimated:YES];
    return false;
}





@end
