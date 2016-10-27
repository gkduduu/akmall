//
//  UWXmlParser.h
//  KissXMLTest
//
//  Created by uniwis on 11. 7. 21..
//  Copyright 2011 korea. All rights reserved.
//

#import <Foundation/Foundation.h>

enum {
    Parser1 = 1,
    Parser2 = 2,
    Parser3 = 3
};
typedef NSUInteger ParserType;


@interface UWXmlParser : NSObject {
    NSMutableData *xmlData;
    
    NSURLConnection *itemConnection;
    
    NSMutableArray *parsedItems;
    
    NSArray *arrItems;
    
    BOOL isComplete;
    ParserType* nParserType;
}

@property (nonatomic, retain) NSMutableData *xmlData;
//@property (nonatomic, retain) NSURLConnection *itemConnection; 
@property (nonatomic, retain) NSMutableArray *parsedItems;

-(id)initParser:(NSURL *)url andWithItems:(NSArray *)items;
-(id)initParser:(NSURL *)url andWithItems:(NSArray *)items  andWithParserType:(ParserType *)ParserType;
-(id)initOtherParser:(NSMutableURLRequest *)urlRequset  andWithItems:(NSArray *)items;
-(id)initOtherParser:(NSMutableURLRequest *)urlRequset  andWithItems:(NSArray *)items andWithParserType:(ParserType *)ParserType;



-(void)initRequest:(NSURLRequest *)req;


@end
