package TestComponents;

import Resources.ExtentReporterNG;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

public class Listeners implements ITestListener {
    ExtentReports extent = ExtentReporterNG.getReportObject();
    Map<String, ExtentTest> classTestMap = new HashMap<>();
    ThreadLocal<ExtentTest> methodLevelTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();

        ExtentTest classNode;
        if (!classTestMap.containsKey(className)) {
            classNode = extent.createTest(className);
            classTestMap.put(className, classNode);
        } else {
            classNode = classTestMap.get(className);
        }
        ExtentTest methodNode = classNode.createNode(methodName);
        methodLevelTest.set(methodNode);

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        methodLevelTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        methodLevelTest.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        methodLevelTest.get().skip("Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
