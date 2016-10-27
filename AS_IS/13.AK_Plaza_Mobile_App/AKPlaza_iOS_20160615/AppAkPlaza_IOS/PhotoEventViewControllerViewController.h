//
//  PhotoEventViewControllerViewController.h
//  AppAkPlaza_IOS
//
//  Created by 김 두민 on 12. 5. 22..
//  Copyright (c) 2012년 puuu80@uniwis.com. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TouchScrollView.h"
#import "BCZeroEdgeTextView.h"

@interface PhotoEventViewControllerViewController : UIViewController <UIActionSheetDelegate,UINavigationControllerDelegate,UIImagePickerControllerDelegate,UIAlertViewDelegate,UIScrollViewDelegate,UITextViewDelegate,UITextFieldDelegate,UIWebViewDelegate>
{
	UIImagePickerController *imgPicker;
    BOOL isJoin;
    NSString *userID;
    NSString *act;
    NSString *type;
    NSString *event_index;
    NSString *event_token;
    NSString *title;
    NSString *contents;
    NSString *entry_indexno;
    NSString *name;
    NSString *phone;
    UIImage *capturedImg;
    float currentContentHeight;
}

@property(nonatomic,retain) IBOutlet UIView *vw_BackWorkingView;
//@property(nonatomic,retain) IBOutlet UILabel *lb_UserID;
@property (retain, nonatomic) IBOutlet UIWebView *header;
@property (retain, nonatomic) IBOutlet UILabel *titlebox;

@property(nonatomic,retain) IBOutlet UITextField *tf_Title;
@property(nonatomic,retain) IBOutlet BCZeroEdgeTextView *tv_Contents;
@property(nonatomic,retain) IBOutlet UIImageView *iv_Attach;
@property(nonatomic,retain) IBOutlet UIButton *btn_Attach_bgView;
@property(nonatomic,retain) IBOutlet UIView *vw_BottomContainer;
@property(nonatomic,retain) IBOutlet TouchScrollView *sclv_ParentContainer;


//@property(nonatomic,retain) IBOutlet UIButton *btn_AddAttach;
//@property(nonatomic,retain) IBOutlet UIButton *btn_Submit;
//@property(nonatomic,retain) IBOutlet UIButton *btn_Cancel;


-(IBAction)actionAddAttach:(id)sender;
-(IBAction)actionSubmit:(id)sender;
-(IBAction)actionCancel:(id)sender;
-(IBAction)actionFirstResponderContentTextView:(id)sender;

-(void) setParamTokensUserID:(NSString*)pUserId act:(NSString*)pAct type:(NSString*)pType event_index:(NSString*)pEvent_index event_token:(NSString*)pEvent_token name:(NSString *) pname phone:(NSString *) pphone;
-(void) setParamTokensForUpdateUserID:(NSString*)pUserId act:(NSString*)pAct type:(NSString*)pType event_index:(NSString*)pEvent_index entry_indexno:(NSString*)pEntry_indexno name:(NSString *) pname phone:(NSString *) pphone;

@end
