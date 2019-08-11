package gapp.season.jar.filerename;

import java.awt.Container;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

@SuppressWarnings("rawtypes,deprecation")
class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int WINDOW_WIDTH = 935;
	private static final int WINDOW_HEIGHT = 590;

	private static MainWindow mMainWindow;
	private JTextPane tOrg;// 原文件列表预览窗口
	private JTextPane tReview;// 预览结果窗口
	private JTextPane tTips;// log输出窗口
	private JTextField tRenameRule;
	private JTextField tNoStart;
	private JTextField tNoAdd;
	private JTextField tReplaceOrg;
	private JTextField tReplaceAfter;
	private JTextField tExtension;
	private JTextField tAddIndex;
	private JTextField tAddStr;
	private JTextField tRemoveIndex;
	private JTextField tRemoveNum;

	private File[] files; //已选择的文件列表
	private boolean showExt; //标记文件窗口是否显示文件大小、修改时间等信息
	private Action actionReview = new ReviewAction();

	static MainWindow getInstance() {
		if (mMainWindow == null) {
			mMainWindow = new MainWindow();
		}
		return mMainWindow;
	}

	/**
	 * 构造方法
	 */
	private MainWindow() {
		initView();
		initData();
	}

	private void initView() {
		JLabel label1 = new JLabel("命名规则(*将替换为原文件名，#将替换为数字编号)", SwingConstants.LEFT);
		JLabel label2 = new JLabel("起始编号 ", SwingConstants.RIGHT);
		JLabel label3 = new JLabel("增量", SwingConstants.RIGHT);
		JLabel label4 = new JLabel("把", SwingConstants.RIGHT);
		JLabel label5 = new JLabel("替换为", SwingConstants.RIGHT);
		JLabel label6 = new JLabel("扩展名更改为", SwingConstants.RIGHT);
		JLabel label7 = new JLabel("在第", SwingConstants.RIGHT);
		JLabel label8 = new JLabel("个字符处添加", SwingConstants.RIGHT);
		JLabel label9 = new JLabel("从第", SwingConstants.RIGHT);
		JLabel label10 = new JLabel("个字符处删除", SwingConstants.RIGHT);
		JLabel label11 = new JLabel("个字符", SwingConstants.RIGHT);
		label1.setBounds(620, 5, 300, 20);
		label2.setBounds(620, 53, 60, 20);
		label3.setBounds(760, 53, 30, 20);
		label4.setBounds(620, 78, 18, 20);
		label5.setBounds(750, 78, 45, 20);
		label7.setBounds(620, 103, 30, 20);
		label8.setBounds(700, 103, 80, 20);
		label9.setBounds(620, 128, 30, 20);
		label10.setBounds(700, 128, 80, 20);
		label11.setBounds(820, 128, 45, 20);
		label6.setBounds(620, 153, 80, 20);

		tOrg = new JTextPane();
		tOrg.setEditable(false);
		tOrg.setFont(new Font(null, Font.PLAIN, 14));
		JScrollPane stOrg = new JScrollPane(tOrg);
		tReview = new JTextPane();
		tReview.setEditable(false);
		tReview.setFont(new Font(null, Font.PLAIN, 14));
		JScrollPane stReview = new JScrollPane(tReview);
		tTips = new JTextPane();
		tTips.setEditable(false);
		tTips.setText("日志窗口\n");
		JScrollPane stTips = new JScrollPane(tTips);
		stOrg.setBounds(8, 5, 600, 270);
		stReview.setBounds(8, 278, 600, 270);
		stTips.setBounds(620, 320, 300, 228);
		resetListShow();

		tRenameRule = new JTextField();
		tRenameRule.setFont(new Font(null, Font.PLAIN, 14));
		tNoStart = new JTextField();
		tNoAdd = new JTextField();
		tReplaceOrg = new JTextField();
		tReplaceAfter = new JTextField();
		tExtension = new JTextField();
		tAddIndex = new JTextField();
		tAddStr = new JTextField();
		tRemoveIndex = new JTextField();
		tRemoveNum = new JTextField();
		tRenameRule.setBounds(620, 28, 300, 20);
		tNoStart.setBounds(685, 53, 60, 20);
		tNoAdd.setBounds(795, 53, 40, 20);
		tReplaceOrg.setBounds(643, 78, 100, 20);
		tReplaceAfter.setBounds(800, 78, 100, 20);
		tAddIndex.setBounds(655, 103, 40, 20);
		tAddStr.setBounds(785, 103, 120, 20);
		tRemoveIndex.setBounds(655, 128, 40, 20);
		tRemoveNum.setBounds(785, 128, 35, 20);
		tExtension.setBounds(705, 153, 80, 20);
		resetRenameSetting();

		//主要功能
		JButton bOpenFiles = new JButton("选择文件列表");
		bOpenFiles.setAction(new OpenAction());// 设置命令按钮对应的初始命令
		bOpenFiles.setRequestFocusEnabled(false); // 设置不需要焦点
		JButton bReview = new JButton("重命名结果预览");
		bReview.setAction(actionReview);
		bReview.setRequestFocusEnabled(false);
		JButton bRename = new JButton("重命名");
		bRename.setAction(new RenameAction());
		bRename.setRequestFocusEnabled(false);
		JButton bHash = new JButton("文件校验码");
		bHash.setAction(new HashAction());
		bHash.setRequestFocusEnabled(false);
		bOpenFiles.setBounds(620, 220, 140, 40);
		bHash.setBounds(780, 220, 140, 40);
		bReview.setBounds(620, 270, 140, 40);
		bRename.setBounds(780, 270, 140, 40);
		//辅助功能
		JButton bClear = new JButton();
		bClear.setAction(new AbstractAction("清空列表") {
			@Override
			public void actionPerformed(ActionEvent e) {
				files = null;
				resetListShow();
				println("清空已选择的文件列表完成");
			}
		});
		bClear.setRequestFocusEnabled(false);
		JButton bReset = new JButton();
		bReset.setAction(new AbstractAction("还原设置") {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetRenameSetting();
				println("还原重命名设置完成");
			}
		});
		bReset.setRequestFocusEnabled(false);
		JButton bClearLog = new JButton();
		bClearLog.setAction(new AbstractAction("清空日志") {
			@Override
			public void actionPerformed(ActionEvent e) {
				tTips.setText("日志窗口\n");
			}
		});
		bClearLog.setRequestFocusEnabled(false);
		bClear.setBounds(620, 180, 90, 30);
		bReset.setBounds(720, 180, 90, 30);
		bClearLog.setBounds(820, 180, 90, 30);

		Container container = getContentPane();
		container.setLayout(null);
		container.add(label1);
		container.add(label2);
		container.add(label3);
		container.add(label4);
		container.add(label5);
		container.add(label6);
		container.add(label7);
		container.add(label8);
		container.add(label9);
		container.add(label10);
		container.add(label11);
		container.add(stOrg);
		container.add(stReview);
		container.add(stTips);
		container.add(tRenameRule);
		container.add(tNoStart);
		container.add(tNoAdd);
		container.add(tReplaceOrg);
		container.add(tReplaceAfter);
		container.add(tExtension);
		container.add(tAddIndex);
		container.add(tAddStr);
		container.add(tRemoveIndex);
		container.add(tRemoveNum);
		container.add(bOpenFiles);
		container.add(bReview);
		container.add(bRename);
		container.add(bClear);
		container.add(bReset);
		container.add(bClearLog);
		container.add(bHash);

		//拖拽文件操作
		new DropTarget(tOrg, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				try {
					// 如果拖入的文件格式受支持
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						// 接收拖拽来的数据
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						@SuppressWarnings("unchecked")
						List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
						int lastSize = (files == null ? 0 : files.length);
						ArrayList<File> newList = new ArrayList<>();
						for (int i = 0; i < (lastSize + list.size()); i++) {
							File f;
							if (i < lastSize) {
								f = files[i];
							} else {
								f = list.get(i - lastSize);
							}
							if (!newList.contains(f)) {
								newList.add(f);
							}
						}
						files = newList.toArray(new File[0]);
						showFiles();
						// 指示拖拽操作已完成
						dtde.dropComplete(true);
						println("拖拽文件到列表成功，拖入" + list.size() + "个文件");
					} else {
						// 拒绝拖拽来的数据
						dtde.rejectDrop();
						println("拖拽过来的数据格式不支持");
					}
				} catch (Exception e) {
					e.printStackTrace();
					printlnError("拖拽过来的数据解析失败");
				}
			}
		}).setActive(true);
	}

	private void initData() {
		println("文件批量重命名工具启动...");
		setTitle("文件批量重命名工具");// 设置窗体标题
		//setIconImage(Constants.ICON);// 设置窗体图标
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);// 设置窗体(包括边框)尺寸
		setLocationRelativeTo(null);// 设置窗体中心位置对准屏幕中心
		setVisible(true);// 设置窗体可见，默认为false不可见
		setResizable(false);// 设置窗体可变性，默认为true窗体大小可变
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭窗口时退出程序
		// 自定义关闭窗口事件
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitProject();
			}
		});
		showExt = false;
	}

	private class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		OpenAction() {
			super("选择文件列表");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser(/*lastDir*/);
			//filechooser.setFileFilter(new FileTypeFilter()); //文件类型筛选
			filechooser.setMultiSelectionEnabled(true);
			int i = filechooser.showOpenDialog(mMainWindow);
			if (i == JFileChooser.APPROVE_OPTION) {
				files = filechooser.getSelectedFiles();
				showFiles();
				println("选择了" + (files == null ? 0 : files.length) + "个文件");
			}
		}
	}

	private class ReviewAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		ReviewAction() {
			super("重命名结果预览");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				StringBuilder sb = new StringBuilder("重命名结果预览：\n");
				if (files != null && files.length > 0) {
					int index = 0;
					for (File f : files) {
						index++;
						sb.append(index).append(". ").append(getRenameStr(f.getName(), index - 1)).append("\n");
					}
					println("重命名结果预览完成");
				}
				tReview.setText(sb.toString().trim());
			} catch (Exception ex) {
				ex.printStackTrace();
				printlnError("重命名结果预览异常");
			}
		}
	}

	private class RenameAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		RenameAction() {
			super("重命名");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (files != null && files.length > 0) {
				actionReview.actionPerformed(e); //重命名前先进行预览
				int confirm = JOptionPane.showConfirmDialog(mMainWindow, "重命名操作不可逆，您确认要重命名选择的文件吗？", "重命名", JOptionPane.OK_CANCEL_OPTION);
				if (confirm == JOptionPane.OK_OPTION) {
					//重命名操作（重命名完成后更新原文件列表展示）
					int successNum = 0, failNum = 0;
					if (files != null && files.length > 0) {
						int index = 0;
						for (File f : files) {
							index++;
							String newName = getRenameStr(f.getName(), index - 1);
							File newFile = new File(f.getParent(), newName);
							boolean result = f.renameTo(newFile);
							if (!result) {
								failNum++;
								printlnError("重命名文件失败： " + f.getAbsolutePath());
							} else {
								successNum++;
								files[index - 1] = newFile;
							}
						}
						showFiles();
					}
					println(String.format(Locale.getDefault(), "重命名完成，成功%d个，失败%d个", successNum, failNum));
				}
			} else {
				println("请选择需要重命名的文件");
			}
		}
	}

	private class HashAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		HashAction() {
			super("文件校验码");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//主线程耗时操作、会卡一段时间的UI
				StringBuilder sb = new StringBuilder("文件校验码：\n\n");
				if (files != null && files.length > 0) {
					int index = 0;
					for (File f : files) {
						index++;
						sb.append(index).append(". 文件名称： ").append(f.getAbsolutePath()).append("\n");
						sb.append("文件大小： ").append(f.length()).append(" 字节\n");
						sb.append("修改时间： ").append(new Date(f.lastModified()).toLocaleString()).append("\n");
						sb.append("MD5： ").append(HashUtil.encode(new FileInputStream(f), HashUtil.ALGORITHM_MD5)).append("\n");
						sb.append("SHA1： ").append(HashUtil.encode(new FileInputStream(f), HashUtil.ALGORITHM_SHA_1)).append("\n");
						sb.append("\n");
					}
					println("生成文件校验码完成");
				}
				tReview.setText(sb.toString().trim());
			} catch (Exception ex) {
				ex.printStackTrace();
				printlnError("生成文件校验码异常");
			}
		}
	}

	private void resetListShow() {
		//文件排序：拖拽方式添加文件会按目录当前的顺序排序(推荐)，JFileChooser方式添加的文件列表固定按文件名排序
		tOrg.setText("原文件列表：(未选择文件)\n\n添加文件方式：\n1. 拖拽文件到此窗口添加要操作的文件(推荐)；\n2. 点击‘选择文件列表’按钮选择要操作的文件；");
		StringBuilder tips;
		tips = new StringBuilder("预览窗口\n");
		tips.append("\n1. 展示窗口提示\n");
		tips.append("原文件列表窗口中展示的为已选择的文件列表；\n");
		tips.append("预览窗口中展示重命名及校验码预览(文件重命名结果预览及文件校验码)；\n");
		tips.append("日志窗口展示程序启动后各项操作的日志信息；\n");
		tips.append("\n2. 重命名规则设置\n");
		tips.append("例如命名规则框输入'*-#'，则重命名后的文件名为'<原文件名>-<数字编号>'；\n");
		tips.append("起始编号格式为'1/01/001/……'，起始编号的长度表示位数；\n");
		tips.append("编号增量表示数字编号的间隔，如起始编号为'0001',增量为'2'，则编号为：0001,0003,0005,……；\n");
		tips.append("替换表示文件名中对应字符串替换为新的字符串，添加/删除表示在文件名指定位置增删字符串；\n");
		tips.append("扩展名框中输入文件新的扩展名后缀，例如：'txt' 或 '.txt'；\n");
		tips.append("规则设置完成后可以点击‘重命名结果预览’按钮预览重命名后的文件名；\n");
		tips.append("\n3. 功能按钮提示\n");
		tips.append("‘清空列表’表示清空已选择的文件列表、‘还原设置’表示还原重命名规则、‘清空日志’表示清空日志窗口；\n");
		tips.append("点击‘选择文件列表’按钮或拖拽文件到窗口中；\n");
		tips.append("点击‘文件校验码’按钮预览已选择文件的md5/sha1等校验码信息；\n");
		tips.append("点击‘重命名结果预览’按钮预览文件按重命名规则处理后的文件名；\n");
		tips.append("点击‘重命名’按钮开始批量重命名文件；");
		tReview.setText(tips.toString());
	}

	private void resetRenameSetting() {
		tRenameRule.setText("*");
		tNoStart.setText("001");
		tNoAdd.setText("1");
		tReplaceOrg.setText("");
		tReplaceAfter.setText("");
		tExtension.setText("");
		tAddIndex.setText("0");
		tAddStr.setText("");
		tRemoveIndex.setText("0");
		tRemoveNum.setText("0");
	}

	private void showFiles() {
		StringBuilder sb = new StringBuilder("原文件列表：");
		int start = sb.length();
		sb.append("[目录]"); //占位符，在获取到实际目录后替换掉
		int end = sb.length();
		sb.append("\n");
		String dir = "--";
		if (files != null && files.length > 0) {
			dir = files[0].getParent();
			int index = 0;
			for (File f : files) {
				index++;
				String nameShow;
				if (showExt) {
					nameShow = String.format(Locale.getDefault(), "%d. %s (%s字节, %s)",
							index, f.getName(), f.length(), new Date(f.lastModified()).toLocaleString());
				} else {
					nameShow = String.format(Locale.getDefault(), "%d. %s", index, f.getName());
				}
				sb.append(nameShow).append("\n");
				if (dir == null || !dir.equals(f.getParent())) {
					//文件列表不在同一个目录
					dir = "--";
				}
			}
		}
		sb.replace(start, end, String.format(Locale.getDefault(), "(目录：%s)", dir));
		tOrg.setText(sb.toString());
	}

	private String getRenameStr(String orgName, int index) {
		//重命名规则
		String rule = tRenameRule.getText();
		rule = FileUtil.isEmpty(rule) ? "*" : rule;
		//编号替换
		if (rule.contains("#")) {
			int numStart = 1, numCount = 1, numAdd = 1;
			String noStart = tNoStart.getText();
			String noAdd = tNoAdd.getText();
			try {
				numStart = Integer.valueOf(noStart);
				numCount = noStart.length();
				numAdd = Integer.valueOf(noAdd);
			} catch (Exception e) {
				e.printStackTrace();
				printlnError("编号解析异常：请将起始编号和增量设置为数字");
			}
			DecimalFormat df = new DecimalFormat();
			StringBuilder numFormat = new StringBuilder();
			for (int j = 0; j < numCount; j++) {
				numFormat.append("0");
			}
			df.applyPattern(numFormat.toString());
			String numStr = df.format(numStart + index * numAdd);
			rule = FileUtil.replaceStr(rule, '#', numStr);
		}
		//原文件名替换
		if (rule.contains("*")) {
			String filename = FileUtil.getFileNameWithoutExtName(orgName);
			rule = FileUtil.replaceStr(rule, '*', filename);
		}
		//字符串替换
		String replaceOrg = tReplaceOrg.getText();
		String replaceAfter = tReplaceAfter.getText();
		if (!FileUtil.isEmpty(replaceOrg)) {
			rule = FileUtil.replaceStr(rule, replaceOrg, replaceAfter);
		}
		//扩展添加
		String addIndex = tAddIndex.getText();
		String addStr = tAddStr.getText();
		if (!FileUtil.isEmpty(addIndex) && !FileUtil.isEmpty(addStr)) {
			try {
				int addIndexInt = Integer.valueOf(addIndex);
				if (addIndexInt <= 0) {
					rule = addStr + rule;
				} else if (rule.length() <= addIndexInt) {
					rule = rule + addStr;
				} else {
					rule = rule.substring(0, addIndexInt) + addStr + rule.substring(addIndexInt);
				}
			} catch (Exception e) {
				e.printStackTrace();
				printlnError("扩展添加-解析异常：请将字符index设置为数字");
			}
		}
		//扩展删除
		String removeIndex = tRemoveIndex.getText();
		String removeNum = tRemoveNum.getText();
		if (!FileUtil.isEmpty(removeIndex) && !FileUtil.isEmpty(removeNum)) {
			try {
				int removeIndexInt = Integer.valueOf(removeIndex);
				int removeNumInt = Integer.valueOf(removeNum);
				char[] ruleChars = rule.toCharArray();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < ruleChars.length; i++) {
					if (i < removeIndexInt || i >= (removeIndexInt + removeNumInt)) {
						sb.append(ruleChars[i]);
					}
				}
				rule = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
				printlnError("扩展删除-解析异常：请将字符index和num设置为数字");
			}
		}
		if (rule.length() < 1) {
			printlnError("重命名后的文件名为空，请重新设置规则 - " + orgName);
		}
		//更新后缀
		String extension = tExtension.getText();
		if (!FileUtil.isEmpty(extension)) {
			extension = extension.startsWith(".") ? extension : ("." + extension);
			rule = rule + extension;
		} else {
			String ext = FileUtil.getExtName(orgName);
			if (!FileUtil.isEmpty(ext)) {
				rule = rule + "." + ext;
			}
		}
		//重命名结果
		return rule;
	}

	private void println(String msg) {
		tTips.setText(tTips.getText() + "i: " + msg + "\n");
	}

	private void printlnError(String msg) {
		tTips.setText(tTips.getText() + "e: " + msg + "\n");
	}

	private void exitProject() {
		if (files != null && files.length > 0) {
			int confirm = JOptionPane.showConfirmDialog(mMainWindow, "您确认要退出吗？", "退出确认", JOptionPane.OK_CANCEL_OPTION);
			if (confirm == JOptionPane.OK_OPTION) {
				System.exit(0);// 退出程序
			}
		} else {
			System.exit(0);// 退出程序
		}
	}
}
