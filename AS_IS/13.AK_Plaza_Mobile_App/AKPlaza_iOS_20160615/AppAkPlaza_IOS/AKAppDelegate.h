//
//  AKAppDelegate.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWTabBarView.h"
#import "AlertMassageUtil.h"
#import "CommonMsg.h"
#import "CommonURL.h"
#import "OLogin.h"
#import "OLogin+UserDefaults.h"
#import "AkLoginModel.h"
#import "Reachability.h"
#import "AkMainModel.h"


@interface AKAppDelegate : UIResponder <UIApplicationDelegate, UIAlertViewDelegate>
{
    
    
    UWTabBarView *tabView;
    UIImageView *activityImageView;
    
    
}

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, retain) UWTabBarView *tabView;
@property (nonatomic, retain) UIImageView *activityImageView;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
//p65458 20130620 add
@property (nonatomic, retain) NSArray *arrNotilist;
@property int nPageNum;
@property (nonatomic, retain) NSString* pushid;
@property (nonatomic, retain) NSString* bc;
//p65458 20130620 add

@property (nonatomic) CFURLRef baseURL;

//p65458 20130708 add
@property(nonatomic,retain) UIView *view; 
//p65458 20130708 add


- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end
