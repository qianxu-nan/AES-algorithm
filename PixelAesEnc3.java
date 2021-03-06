import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.*;
import java.awt.image.Raster;
import java.lang.*;
import java.awt.Color;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 



class  PixelAesEnc3
{
	public static int[][] rgbArray(int[] xx)
	{
		int [][] plaintext=null;
		if (xx.length%16==0)
	{
		
		plaintext=new int[xx.length/16][16];
		for(int m = 0; m < xx.length/16; m ++) 
         	{
            	for(int s = 0; s < 16; s ++) 
    			{
               		plaintext[m][s]=xx[m*16+s];
				}
			}
	}	
	else
	{
		
		plaintext=new int[xx.length/16+1][16];
		
		for(int m = 0; m < xx.length/16; m ++) 
         	{
            	for(int s = 0; s < 16; s ++) 
    			{
               		plaintext[m][s]=xx[m*16+s];
				}

			}
			for(int s = 0; s < xx.length%16; s ++) 
    			{
               		plaintext[xx.length/16][s]=xx[xx.length-(xx.length%16-s)];
				}
			for(int s = xx.length%16; s < 16; s ++) 
    			{
               		plaintext[xx.length/16][s]=114;
				}
	
	}


    		return plaintext;
	}

	public static int[] key16(int[] xc,int round )
	{
		int [][] keyround=new int[16][8];
		for(int a=0; a<16;a++)
		{
			int temp1 = xc[a];
			for (int b=0; b<8;b++)
				{   
					keyround[a][7-b]= temp1&1;
					temp1 =temp1>>>1;
				}
		}
		int[] keyround_0=new int[128];
		for(int e= 0; e< 16; e ++)
			{
				for(int f = 0; f< 8; f ++) 
					{
						keyround_0[e * 8 + f] = keyround[e][f];
					}	
			}
		
//	****************************************************************************
	//Circular byte left Shift the last word of the Round-0 Key to get A:
		int[] temp2=new int[8];
		for(int z=0; z<8; z++)
		{
			temp2[z]=keyround[12][z];
		}
		
		for(int z=0; z<8; z++)
		{
			keyround[12][z]=keyround[13][z];
			keyround[13][z]=keyround[14][z];
			keyround[14][z]=keyround[15][z];
			keyround[15][z]=temp2[z];
		}
//**********************************************************************************
//	 Byte Substitution (S-Box): (B7;5A;9D;85)  --- B:	
		int[][]s_box= {{0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76},
					  {0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0},
					  {0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15},
					  {0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75},
					  {0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84},
					  {0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf},
					  {0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8},
					  {0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2},
					  {0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x10,0x73},
					  {0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb},
					  {0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79},
					  {0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08},
					  {0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a},
					  {0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e},
					  {0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf},
		              {0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16}};
		
		int[]row=new int[4];
		int[]column=new int[4];
		
		row[0]=keyround[12][0]*8+keyround[12][1]*4+keyround[12][2]*2+keyround[12][3];
		row[1]=keyround[13][0]*8+keyround[13][1]*4+keyround[13][2]*2+keyround[13][3];
		row[2]=keyround[14][0]*8+keyround[14][1]*4+keyround[14][2]*2+keyround[14][3];
		row[3]=keyround[15][0]*8+keyround[15][1]*4+keyround[15][2]*2+keyround[15][3];
		
		column[0]=keyround[12][4]*8+keyround[12][5]*4+keyround[12][6]*2+keyround[12][7];
		column[1]=keyround[13][4]*8+keyround[13][5]*4+keyround[13][6]*2+keyround[13][7];
		column[2]=keyround[14][4]*8+keyround[14][5]*4+keyround[14][6]*2+keyround[14][7];
		column[3]=keyround[15][4]*8+keyround[15][5]*4+keyround[15][6]*2+keyround[15][7];
		
		int[]B=new int[4];
		for(int xm=0; xm<4;xm++)
		{
			B[xm]=s_box[row[xm]][column[xm]];
		}
//************************************************************************************************
//Adding round constant : T
		int[]T=new int[4];
		int[][]R1={{1,0,0,0},{2,0,0,0},{4,0,0,0},{8,0,0,0},{16,0,0,0},{32,0,0,0},{64,0,0,0},{128,0,0,0},{27,0,0,0},{54,0,0,0}};
		for(int y=0; y<4;y++)
		{
			T[y]=B[y]^R1[round][y];
			
		}
//********************************************************
// round1 keyround
		int[]w4=new int[4];
		for(int x1=0; x1<4; x1++)
		{
			w4[x1]=xc[x1]^T[x1];
		}
			int[]w5=new int[4];
		for(int x2=0; x2<4; x2++)
		{
			w5[x2]=xc[x2+4]^w4[x2];
		}
			int[]w6=new int[4];
		for(int x3=0; x3<4; x3++)
		{
			w6[x3]=xc[x3+8]^w5[x3];
		}
		
			int[]w7=new int[4];
		for(int x4=0; x4<4; x4++)
		{
			w7[x4]=xc[x4+12]^w6[x4];
		}
		int[]subkey= new int[16];
		for(int x5=0; x5<4; x5++)
		{
			subkey[x5]=w4[x5];
			subkey[x5+4]=w5[x5];
			subkey[x5+8]=w6[x5];
			subkey[x5+12]=w7[x5];
		}
	
		return subkey;
	}
	
	public static int[][] sub_key1(String args1)
	{
		byte [] Pasc=args1.getBytes();
		int[] Pasc1=new int[16];
		for(int x=0; x<16; x++)
				
		{
			Pasc1[x]=Pasc[x];
		}
	
			System.out.println("the sub_key for each round:");
			int[][]sub_key=new int[11][16];
			for(int x=0; x<16; x++)
				
		{
			sub_key[0][x]= Pasc1[x];
		}
			
			sub_key[1]= key16(Pasc1,0);

			for(int x=2; x<11; x++)
			{
				sub_key[x]= key16(sub_key[x-1],x-1);
				
			}
	
			
		for(int x=0; x<11; x++)
		{System.out.print("sub_key"+x+":\t");
			for(int y=0; y<16; y++)
			{
			System.out.print(Integer.toHexString(sub_key[x][y])+" ");
			}
			System.out.println();
		}
		return sub_key;
	}
	public static int[]aencryp(int[] cc,int[]kk)
	
	{
		int[][]s1_box={{0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76},
					  {0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0},
					  {0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15},
					  {0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75},
					  {0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84},
					  {0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf},
					  {0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8},
					  {0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2},
					  {0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x10,0x73},
					  {0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb},
					  {0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79},
					  {0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08},
					  {0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a},
					  {0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e},
					  {0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf},
		              {0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16}};

		int[][] plaintext1=new int[16][8];
		
		for(int a=0; a<16;a++)
		{
			int temp11 = cc[a];
			for (int b=0; b<8;b++)
				{   
					plaintext1[a][7-b]= temp11&1;
					temp11 =temp11>>>1;
				}
		}
		int[]row1=new int[16];
		int[]column1=new int[16];
		for(int h=0; h<16; h++)
		{
			row1[h]=plaintext1[h][0]*8+plaintext1[h][1]*4+plaintext1[h][2]*2+plaintext1[h][3];
			column1[h]=plaintext1[h][4]*8+plaintext1[h][5]*4+plaintext1[h][6]*2+plaintext1[h][7];
		}

		int[]subbyte=new int[16];
		
		 for(int xmm=0; xmm<16;xmm++)
		{
			subbyte[xmm]=s1_box[row1[xmm]][column1[xmm]];
			
		}
		int temp7=subbyte[1];
		subbyte[1]=subbyte[5];
		subbyte[5]=subbyte[9];
		subbyte[9]=subbyte[13];
		subbyte[13]=temp7;
		int temp8=subbyte[2];
		int temp9=subbyte[6];
		subbyte[2]=subbyte[10];
		subbyte[6]=subbyte[14];
		subbyte[10]=temp8;
		subbyte[14]=temp9;
		int temp10=subbyte[15];
		subbyte[15]=subbyte[11];
		subbyte[11]=subbyte[7];
		subbyte[7]=subbyte[3];
		subbyte[3]=temp10;
		int[][]e_table={{0x01,0x03,0x05,0x0f,0x11,0x33,0x55,0xff,0x1a,0x2e,0x72,0x96,0xa1,0xf8,0x13,0x35},
					  	{0x5f,0xe1,0x38,0x48,0xd8,0x73,0x95,0xa4,0xf7,0x02,0x06,0x0a,0x1e,0x22,0x66,0xaa},
					  	{0xe5,0x34,0x5c,0xe4,0x37,0x59,0xeb,0x26,0x6a,0xbe,0xd9,0x70,0x90,0xab,0xe6,0x31},
					  	{0x53,0xf5,0x04,0x0c,0x14,0x3c,0x44,0xcc,0x4f,0xd1,0x68,0xb8,0xd3,0x6e,0xb2,0xcd},
					  	{0x4c,0xd4,0x67,0xa9,0xe0,0x3b,0x4d,0xd7,0x62,0xa6,0xf1,0x08,0x18,0x28,0x78,0x88},
					  	{0x83,0x9e,0xb9,0xd0,0x6b,0xbd,0xdc,0x7f,0x81,0x98,0xb3,0xce,0x49,0xdb,0x76,0x9a},
					 	{0xb5,0xc4,0x57,0xf9,0x10,0x30,0x50,0xf0,0x0b,0x1d,0x27,0x69,0xbb,0xd6,0x61,0xa3},
					 	{0xfe,0x19,0x2b,0x7d,0x87,0x92,0xad,0xec,0x2f,0x71,0x93,0xae,0xe9,0x20,0x60,0xa0},
					 	{0xfb,0x16,0x3a,0x4e,0xd2,0x6d,0xb7,0xc2,0x5d,0xe7,0x32,0x56,0xfa,0x15,0x3f,0x41},
					  	{0xc3,0x5e,0xe2,0x3d,0x47,0xc9,0x40,0xc0,0x5b,0xed,0x2c,0x74,0x9c,0xbf,0xda,0x75},
					 	{0x9f,0xba,0xd5,0x64,0xac,0xef,0x2a,0x7e,0x82,0x9d,0xbc,0xdf,0x7a,0x8e,0x89,0x80},
					 	{0x9b,0xb6,0xc1,0x58,0xe8,0x23,0x65,0xaf,0xea,0x25,0x6f,0xb1,0xc8,0x43,0xc5,0x54},
					  	{0xfc,0x1f,0x21,0x63,0xa5,0xf4,0x07,0x09,0x1b,0x2d,0x77,0x99,0xb0,0xcb,0x46,0xca},
					 	{0x45,0xcf,0x4a,0xde,0x79,0x8b,0x86,0x91,0xa8,0xe3,0x3e,0x42,0xc6,0x51,0xf3,0x0e},
					  	{0x12,0x36,0x5a,0xee,0x29,0x7b,0x8d,0x8c,0x8f,0x8a,0x85,0x94,0xa7,0xf2,0x0d,0x17},
		             	{0x39,0x4b,0xdd,0x7c,0x84,0x97,0xa2,0xfd,0x1c,0x24,0x6c,0xb4,0xc7,0x52,0xf6,0x01}};	
		


    int [][]cons={{0,0,0,0,0,0,1,0},{0,0,0,0,0,0,1,1},{0,0,0,0,0,0,0,1},{0,0,0,0,0,0,0,1},
				 {0,0,0,0,0,0,0,1},{0,0,0,0,0,0,0,1},{0,0,0,0,0,0,1,1},{0,0,0,0,0,0,0,1},
				 {0,0,0,0,0,0,0,1},{0,0,0,0,0,0,0,1},{0,0,0,0,0,0,1,0},{0,0,0,0,0,0,1,1},
				 {0,0,0,0,0,0,1,1},{0,0,0,0,0,0,0,1},{0,0,0,0,0,0,0,1},{0,0,0,0,0,0,1,0}};
	int[][]l_table={{0xff,0x00,0x19,0x01,0x32,0x02,0x1a,0xc6,0x4b,0xc7,0x1b,0x68,0x33,0xee,0xdf,0x03},
					  {0x64,0x04,0xe0,0x0e,0x34,0x8d,0x81,0xef,0x4c,0x71,0x08,0xc8,0xf8,0x69,0x1c,0xc1},
					  {0x7d,0xc2,0x1d,0xb5,0xf9,0xb9,0x27,0x6a,0x4d,0xe4,0xa6,0x72,0x9a,0xc9,0x09,0x78},
					  {0x65,0x2f,0x8a,0x05,0x21,0x0f,0xe1,0x24,0x12,0xf0,0x82,0x45,0x35,0x93,0xda,0x8e},
					  {0x96,0x8f,0xdb,0xbd,0x36,0xd0,0xce,0x94,0x13,0x5c,0xd2,0xf1,0x40,0x46,0x83,0x38},
					  {0x66,0xdd,0xfd,0x30,0xbf,0x06,0x8b,0x62,0xb3,0x25,0xe2,0x98,0x22,0x88,0x91,0x10},
					  {0x7e,0x6e,0x48,0xc3,0xa3,0xb6,0x1e,0x42,0x3a,0x6b,0x28,0x54,0xfa,0x85,0x3d,0xba},
					  {0x2b,0x79,0x0a,0x15,0x9b,0x9f,0x5e,0xca,0x4e,0xd4,0xac,0xe5,0xf3,0x73,0xa7,0x57},
					  {0xaf,0x58,0xa8,0x50,0xf4,0xea,0xd6,0x74,0x4f,0xae,0xe9,0xd5,0xe7,0xe6,0xad,0xe8},
					  {0x2c,0xd7,0x75,0x7a,0xeb,0x16,0x0b,0xf5,0x59,0xcb,0x5f,0xb0,0x9c,0xa9,0x51,0xa0},
					  {0x7f,0x0c,0xf6,0x6f,0x17,0xc4,0x49,0xec,0xd8,0x43,0x1f,0x2d,0xa4,0x76,0x7b,0xb7},
					  {0xcc,0xbb,0x3e,0x5a,0xfb,0x60,0xb1,0x86,0x3b,0x52,0xa1,0x6c,0xaa,0x55,0x29,0x9d},
					  {0x97,0xb2,0x87,0x90,0x61,0xbe,0xdc,0xfc,0xbc,0x95,0xcf,0xcd,0x37,0x3f,0x5b,0xd1},
					  {0x53,0x39,0x84,0x3c,0x41,0xa2,0x6d,0x47,0x14,0x2a,0x9e,0x5d,0x56,0xf2,0xd3,0xab},
					  {0x44,0x11,0x92,0xd9,0x23,0x20,0x2e,0x89,0xb4,0x7c,0xb8,0x26,0x77,0x99,0xe3,0xa5},
		              {0x67,0x4a,0xed,0xde,0xc5,0x31,0xfe,0x18,0x0d,0x63,0x8c,0x80,0xc0,0xf7,0x70,0x07}};

int[][]tempp=new int[16][8];
	
		for(int aa8=0; aa8<16;aa8++)
		{
			int temp15 = subbyte[aa8];
			for (int bb7=0; bb7<8;bb7++)
				{   
					tempp[aa8][7-bb7]= temp15&1;
					temp15 =temp15>>>1;
				}
		}

int[]row2=new int[16];
		int[]column2=new int[16];
		
		for(int h1=0; h1<16; h1++)
		{
			row2[h1]=tempp[h1][0]*8+tempp[h1][1]*4+tempp[h1][2]*2+tempp[h1][3];
			column2[h1]=tempp[h1][4]*8+tempp[h1][5]*4+tempp[h1][6]*2+tempp[h1][7];
		}

		int[]ep=new int[64];
		int []lp=new int[16];
		int[]row3=new int[16];
		int[]column3=new int[16];
		int []lc=new int[16];
		int[] templ=new int[64];
		int[]row4=new int[64];
		int[]column4=new int[64];
		int[]ep=new int[64];

		for(int h=0; h<64;h++)
		{
			for(int h3=0; h3<16;h3++)
			{
				if (row2[h3]==0&column2[h3]==0)
				{
					ep[h]=0;
				}
				else
				{
					lp[h3]=l_table[row2[h3]][column2[h3]];
					row3[h3]=cons[h3][0]*8+cons[h3][1]*4+cons[h3][2]*2+cons[h3][3];
					column3[h3]=cons[h3][4]*8+cons[h3][5]*4+cons[h3][6]*2+cons[h3][7];
					lc[h3]=l_table[row3[h3]][column3[h3]];
					for(int h6=0; h6<4;h6++)
					{
						templ[h6]=lp[h6]+lc[h6];
						templ[h6+4]=lp[h6+4]+lc[h6];
						templ[h6+8]=lp[h6+8]+lc[h6];
						templ[h6+12]=lp[h6+12]+lc[h6];
						templ[h6+16]=lp[h6]+lc[h6+4];
						templ[h6+20]=lp[h6+4]+lc[h6+4];
						templ[h6+24]=lp[h6+8]+lc[h6+4];
						templ[h6+28]=lp[h6+12]+lc[h6+4];
						templ[h6+32]=lp[h6]+lc[h6+8];
						templ[h6+36]=lp[h6+4]+lc[h6+8];
						templ[h6+40]=lp[h6+8]+lc[h6+8];
						templ[h6+44]=lp[h6+12]+lc[h6+8];
						templ[h6+48]=lp[h6]+lc[h6+12];
						templ[h6+52]=lp[h6+4]+lc[h6+12];
						templ[h6+56]=lp[h6+8]+lc[h6+12];
						templ[h6+60]=lp[h6+12]+lc[h6+12];
			
					}
					int[][]temppp=new int[64][8];
		
					for(int aaa8=0; aaa8<64;aaa8++)
					{
						int temp16 = templ[aaa8];
						for (int bbb7=0; bbb7<8;bbb7++)
						{   
							temppp[aaa8][7-bbb7]= temp16&1;
							temp16 =temp16>>>1;
						}
					}
						for(int h11=0; h11<64; h11++)
						{
							row4[h11]=temppp[h11][0]*8+temppp[h11][1]*4+temppp[h11][2]*2+temppp[h11][3];
							column4[h11]=temppp[h11][4]*8+temppp[h11][5]*4+temppp[h11][6]*2+temppp[h11][7];
						}
					ep[h]=e_table[row4[h]][column4[h]];	
		

				}
			}
		}
			
		
		int[] etemp=new int[16];
		
		for(int u=0; u<16; u++)
		{
			etemp[u]=ep[4*u]^ep[4*u+1]^ep[4*u+2]^ep[4*u+3];
			
		}
	
	

		int[]subround=new int[16];
		
		for (int mmx2=0; mmx2<16; mmx2++)
		{
			subround[mmx2]=etemp[mmx2]^kk[mmx2];
		}
		
		return subround;


	}
	


	public static int[] finalEnc(int[]ss, String s)
	{

		int[][]sub_round=new int[11][16];
		for(int t=0; t<16; t++)
		{
		sub_round[0][t]= ss[t]^sub_key1(s)[0][t];
		}
		
		for(int t=1; t<10; t++)		
		{
			sub_round[t]= aencryp(sub_round[t-1],sub_key1(s)[t]);	
			
		}
		

		int[][]s2_box={{0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76},
					  {0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0},
					  {0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15},
					  {0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75},
					  {0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84},
					  {0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf},
					  {0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8},
					  {0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2},
					  {0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x10,0x73},
					  {0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb},
					  {0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79},
					  {0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08},
					  {0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a},
					  {0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e},
					  {0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf},
		              {0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16}};
					  
					  
		int [][] plaintext2=new int[16][8];
		

		int[] temp111 = sub_round[9];
		
		for(int a89=0; a89<16;a89++)
		{
			int temp1111 = temp111[a89];
			for (int b79=0; b79<8;b79++)
				{   
					plaintext2[a89][7-b79]= temp1111&1;
					temp1111 =temp1111>>>1;
				}
		}
		
	
//**************************************************************************************************************
//using the given subbytes transportation table
		int[]row5=new int[16];
		int[]column5=new int[16];
		
		for(int hg=0; hg<16; hg++)
		{
			row5[hg]=plaintext2[hg][0]*8+plaintext2[hg][1]*4+plaintext2[hg][2]*2+plaintext2[hg][3];
			column5[hg]=plaintext2[hg][4]*8+plaintext2[hg][5]*4+plaintext2[hg][6]*2+plaintext2[hg][7];
		}
		
		
		int[]subbyte1=new int[16];
		
		for(int xmmn=0; xmmn<16;xmmn++)
		{
			subbyte1[xmmn]=s2_box[row5[xmmn]][column5[xmmn]];
				
		}	
		
		
//*************************************************************************************
//after shiftrow definition:
		int temph=subbyte1[1];
		subbyte1[1]=subbyte1[5];
		subbyte1[5]=subbyte1[9];
		subbyte1[9]=subbyte1[13];
		subbyte1[13]=temph;
		int temphh=subbyte1[2];
		int temphhh=subbyte1[6];
		subbyte1[2]=subbyte1[10];
		subbyte1[6]=subbyte1[14];
		subbyte1[10]=temphh;
		subbyte1[14]=temphhh;
		int tempv=subbyte1[15];
		subbyte1[15]=subbyte1[11];
		subbyte1[11]=subbyte1[7];
		subbyte1[7]=subbyte1[3];
		subbyte1[3]=tempv;
		
		
		for(int t=0; t<16;t++)
		{
			sub_round[10][t]= sub_key1(s)[10][t] ^ subbyte1[t];
				
		}	
			
		int[]subround10=sub_round[10];

		
		return subround10;
			
	}

	public static void main(String[] args) throws IOException
	{
		  BufferedImage bufferedImage=new BufferedImage(160,160,BufferedImage.TYPE_INT_RGB);        
		  Graphics graphics=bufferedImage.getGraphics();               
		  Image srcImage= ImageIO.read(new File("hw2.jpg"));               
		  graphics.drawImage(srcImage,0,0,160,160,null);        
		  ImageIO.write(bufferedImage,"jpg",new File("hw2new.jpg"));        
 	
		File file = new File("hw2new.jpg");
        BufferedImage img = null;
	
       img = ImageIO.read(file);  
     
   
	   int [][]r=new int[img.getWidth()][img.getHeight()];
       int [][]g=new int[img.getWidth()][img.getHeight()];
       int [][]b=new int[img.getWidth()][img.getHeight()];
	   System.out.println("W="+img.getWidth()+", H="+img.getHeight());

	   
	   for(int i=0; i<img.getWidth();i++) 
	   {
	        for (int j=0;j<img.getHeight();j++ )

	   		{  
		      r[i][j] =(img.getRGB(i,j)>>16)&0xFF;
			  g[i][j]=(img.getRGB(i,j)>>8)&0xff;
			  b[i][j]=(img.getRGB(i,j) &0xFF);	
                 
	   		}
	   } 
//convert two dimension array to one dimension r,g,b array
	    int img_width = img.getWidth();
	   	int img_height = img.getHeight();
        int[] rarray = new int[img_width*img_height];
		int[] garray = new int[img_width*img_height];
		int[] barray = new int[img_width*img_height];
        for(int m = 0; m < img_height; m ++) 
        {
            for(int s = 0; s < img_width; s ++) 
    		{
                rarray[m * img_width + s] = r[s][m];
                garray[m * img_width + s] = g[s][m];
                barray[m * img_width + s] = b[s][m];
			}
		}

		int[][]plaintext_r=rgbArray(rarray);
		int[][]plaintext_g=rgbArray(garray);
		int[][]plaintext_b=rgbArray(barray);


		int[][]plaintext_renc=new int[plaintext_r.length][16];
		int[][]plaintext_genc=new int[plaintext_g.length][16];
		int[][]plaintext_benc=new int[plaintext_b.length][16];
		 
        for(int m = 0; m < plaintext_r.length; m ++) 
		{
			plaintext_renc[m]=finalEnc( plaintext_r[m], args[0]);
			plaintext_genc[m]=finalEnc( plaintext_g[m], args[0]);
			plaintext_benc[m]=finalEnc( plaintext_b[m], args[0]);
		}

		
		int num1 = plaintext_renc.length;
		int num2 = plaintext_renc[0].length;
		int[] rarraynew = new int[num1*num2];
		int[] garraynew = new int[num1*num2];
		int[] barraynew = new int[num1*num2];
        for(int m = 0; m < num1; m ++) 
        {
            for(int s = 0; s < num2; s ++) 
    		{
                rarraynew[m * num2 + s] = plaintext_renc[m][s];
                garraynew[m * num2 + s] = plaintext_genc[m][s];
                barraynew[m * num2 + s] = plaintext_benc[m][s];
			}
		}
		BufferedImage newImage=new BufferedImage(160,160,BufferedImage.TYPE_INT_RGB); 
		
		for(int m = 0; m < rarraynew.length; m ++) 
		{
			int rgb=new Color(rarraynew[m],garraynew[m],barraynew[m]).getRGB();
			newImage.setRGB(m%160,m/160,rgb);  
		}
         File outputfile=new File("hw2-4.jpg");

		ImageIO.write(newImage, "jpg", outputfile);
	
	
	}

		  
		      
       
		
		}