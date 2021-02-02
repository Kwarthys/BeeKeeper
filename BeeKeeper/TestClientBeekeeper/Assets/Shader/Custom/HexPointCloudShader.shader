Shader "Custom/HexPointCloudShader"
{
	Properties{
	  _Radius("Sphere Radius", float) = 1
	}
		SubShader{
	    LOD 200
	    Tags { "RenderType" = "Opaque" }
		//if you want transparency
		//Tags { "Queue" = "Transparent" "RenderType" = "Transparent" }
		Blend SrcAlpha OneMinusSrcAlpha
		Pass {
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#pragma geometry geom
			#pragma target 4.0                  // Use shader model 3.0 target, to get nicer looking lighting
			#include "UnityCG.cginc"
			struct vertexIn {
				float4 pos : POSITION;
				float4 color : COLOR;
				float3 normal : NORMAL;
			};
			struct vertexOut {
				float4 pos : SV_POSITION;
				float4 color : COLOR0;
				float3 normal : NORMAL;
			};
			struct geomOut {
				float4 pos : POSITION;
				float4 color : COLO0R;
				float3 normal : NORMAL;
			};

			float rand(float3 p) {
				return frac(sin(dot(p.xyz, float3(12.9898, 78.233, 45.5432))) * 43758.5453);
			}
			float2x2 rotate2d(float a) {
			   float s = sin(a);
			   float c = cos(a);
			   return float2x2(c,-s,s,c);
			}
			//Vertex shader: computes normal wrt camera
			vertexOut vert(vertexIn i) {
				vertexOut o;
				o.pos = i.pos;// UnityObjectToClipPos(i.pos);
				o.color = i.color;
				o.normal = i.normal;//ObjSpaceViewDir(o.pos);
				return o;
			}

			static const fixed SQRT3_6 = sqrt(3) / 6;
			float _Radius;
			float4 _Color;
			float4 _Color2;
			//Geometry shaders: Creates an equilateral triangle with the original vertex in the orthocenter
			[maxvertexcount(18)]
			void geom(point vertexOut i[1], inout TriangleStream<geomOut> OutputStream)
			{
				geomOut p;
				float a = _ScreenParams.x / _ScreenParams.y; // aspect ratio
				float s = 1.1 * _Radius;
				float s2 = s * SQRT3_6 *a;
				/*
				p.color = i[0].color * _Color;
				p.normal = float3(0, 0, 1);
				p.pos = i[0].pos + float4(0, s2 * 2, 0, 0);
				OutputStream.Append(p);
				p.pos = i[0].pos + float4(-s * 0.5f, -s2, 0, 0);
				OutputStream.Append(p);
				p.pos = i[0].pos + float4(s*0.5f, -s2, 0, 0);
				OutputStream.Append(p);
				OutputStream.RestartStrip();
				*/

				float4 A = float4(0, s/2, 0, 0);
				float4 B = float4(s/2 * cos(0.52), s/4, 0, 0);
				float4 C = float4(s/2 * cos(0.52), -s/4, 0, 0);
				float4 D = float4(0, -s/2, 0, 0);
				float4 E = float4(-s/2 * cos(0.52), -s/4, 0, 0);
				float4 F = float4(-s/2 * cos(0.52), s/4, 0, 0);
				float4 G = float4(0, 0, 0, 0);


				p.color = i[0].color;// *_Color;
				p.normal = float3(0, 0, 1);
				p.pos = UnityObjectToClipPos(i[0].pos + A);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + B);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + G);
				OutputStream.Append(p);
				OutputStream.RestartStrip();


				p.color = i[0].color;// *_Color2;
				p.normal = float3(0, 0, 1);
				p.pos = UnityObjectToClipPos(i[0].pos + B);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + C);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + G);
				OutputStream.Append(p);
				OutputStream.RestartStrip();


				p.color = i[0].color;// *_Color2;
				p.normal = float3(0, 0, 1);
				p.pos = UnityObjectToClipPos(i[0].pos + C);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + D);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + G);
				OutputStream.Append(p);
				OutputStream.RestartStrip();


				p.color = i[0].color;// *_Color2;
				p.normal = float3(0, 0, 1);
				p.pos = UnityObjectToClipPos(i[0].pos + D);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + E);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + G);
				OutputStream.Append(p);
				OutputStream.RestartStrip();


				p.color = i[0].color;// *_Color2;
				p.normal = float3(0, 0, 1);
				p.pos = UnityObjectToClipPos(i[0].pos + E);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + F);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + G);
				OutputStream.Append(p);
				OutputStream.RestartStrip();


				p.color = i[0].color;// *_Color2;
				p.normal = float3(0, 0, 1);
				p.pos = UnityObjectToClipPos(i[0].pos + F);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + A);
				OutputStream.Append(p);
				p.pos = UnityObjectToClipPos(i[0].pos + G);
				OutputStream.Append(p);
				OutputStream.RestartStrip();
				
			}
			float4 frag(geomOut i) : COLOR
			{
				return i.color;
			// could do some additional lighting calculation here based on normal
			}
			ENDCG
		}
	}
		FallBack "Diffuse"
}
