//
//  AkNotiListBoxViewController.h
//  AppAkPlaza_iOS
//
//  Created by uniwis on 11. 6. 13..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "UWTabBar.h"
#import "AkNotiModel.h"


@interface AkNotiListBoxViewController : UWNavigation <baseModelDelegate,UITableViewDelegate, UITableViewDataSource, UITabBarControllerDelegate> {
    
    UITableView *tableView;
    
    NSArray					*arrNotilist;
    
    int nPageNum;
    
    UIActivityIndicatorView *activityView;
    
    
    AkNotiModel *oModel;
    
    
    //  Reloading var should really be your tableviews datasource
	//  Putting it here for demo purposes 
	BOOL _reloading;
    
    

    
} 

@property (nonatomic, retain) AkNotiModel *oModel;
@property (nonatomic, retain) UITableView *tableView;
@property (nonatomic, retain) NSArray *arrNotilist;
@property (nonatomic, retain) NSString* pushid;

@property int nPageNum;





@property (nonatomic, retain) UIActivityIndicatorView *activityView;



@end
